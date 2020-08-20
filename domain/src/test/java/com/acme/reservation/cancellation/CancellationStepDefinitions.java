package com.acme.reservation.cancellation;

import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.cancellation.helpers.MockTransaction;
import com.acme.reservation.cancellation.helpers.ReservationMockData;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class CancellationStepDefinitions {
  private DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  @Autowired private CancelReservationAsCustomerUseCase cancelReservationAsCustomerUseCase;
  @Autowired private ReservationMockData reservationMockData;
  Mono<RefundBreakdown> refundBreakdownMono;

  @Before
  public void cleanUp() {
    reservationMockData.cleanUpMocks();
  }

  @DataTableType
  public CreateReservationDto createReservationDto(Map<String, String> entry) {

    CreateReservationDto createReservationDto = new CreateReservationDto();
    createReservationDto.setCustomer(createRandomCustomer());
    createReservationDto.setDestination(
        createDestinationInTimeZone(entry.get("DestinationTimeZone")));
    createReservationDto.setStartDate(date(entry.get("ReservationStartDate")));
    createReservationDto.setEndDate(date(entry.get("ReservationEndDate")));
    createReservationDto.setReservationPrice(money(entry.get("TotalReservationPrice")));
    createReservationDto.setCancellationPolicy(cancellationPolicy(entry.get("CancellationPolicy")));

    return createReservationDto;
  }

  @ParameterType(".*")
  public LocalDateTime date(String date) {
    return LocalDateTime.parse(date, dTF);
  }

  @ParameterType(".*") // TODO look at a cleaner way to handle those params
  public CancellationPolicy cancellationPolicy(String value) {
    return CancellationPolicy.valueOf(value);
  }

  @ParameterType(".*")
  public Money money(String value) {
    return new Money(BigDecimal.valueOf(Double.parseDouble(value)));
  }

  @ParameterType(".*")
  public ZoneId timezone(String value) {
    return ZoneId.of(value);
  }

  private CreateReservationDto createReservationDto;

  @Given("that the customer has the following reservation")
  public void initReservation(List<CreateReservationDto> reservationList) {
    Assert.assertTrue("There is at least one reservation defined", reservationList.size() > 0);
    createReservationDto = reservationList.get(0);
  }

  @When("the customer cancels the reservation on {date} in {timezone}")
  public void theCustomerCancelsTheReservationOnReservationCancellationTime(
      LocalDateTime cancellationDate, ZoneId timezone) {
    Reservation reservation = new Reservation(createReservationDto);
    reservation.setReservationId(reservationMockData.randomReservationId());
    configureMocks(reservation);
    reservationMockData.simulateClock(cancellationDate, timezone);
    refundBreakdownMono =
        this.cancelReservationAsCustomerUseCase.cancelAsCustomer(reservation.getReservationId());
  }

  @Then("the customer should receive {money}")
  public void theCustomerShouldReceiveTotalRefundedMoney(Money expectedRefund) {

    StepVerifier.create(refundBreakdownMono)
        .assertNext(
            r ->
                Assert.assertEquals(
                    expectedRefund.getPrice().doubleValue(),
                    r.getAmountToRefund().getPrice().doubleValue(),
                    0.00001))
        .verifyComplete();
  }

  private Customer createRandomCustomer() {
    return new Customer();
  }

  private void configureMocks(Reservation reservation) {
    MockTransaction mockTransaction = new MockTransaction();
    reservationMockData.configureTransaction(mockTransaction);
    reservationMockData.simulateFinanceGatewaySucceeds(reservation);
    reservationMockData.updateStatusSucceeds(reservation);
    reservationMockData.mockGetReservation(reservation);
  }

  private Destination createDestinationInTimeZone(String destinationTimeZone) {
    ZoneId timezone = ZoneId.of(destinationTimeZone);
    Destination destination = new Destination();
    destination.setId(RandomUtils.nextLong());
    destination.setName(RandomStringUtils.randomAlphabetic(5));
    destination.setTimeZone(timezone);
    return destination;
  }
}
