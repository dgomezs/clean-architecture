package com.acme.reservation.cancellation;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.cancellation.helpers.MockTransaction;
import com.acme.reservation.cancellation.helpers.ReservationMockData;
import com.acme.reservation.dto.CreateReservationDto;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
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

  private CreateReservationDto createReservationDto;

  @Given("that the customer has a reservation starting on {date} and ending on {date}")
  public void initReservation(
      LocalDateTime reservationStartDate, LocalDateTime reservationEndDate) {
    createReservationDto = new CreateReservationDto();
    createReservationDto.setStartDate(reservationStartDate);
    createReservationDto.setEndDate(reservationEndDate);
    createReservationDto.setCustomer(createRandomCustomer());
  }

  @And("that the reservation is for {money}")
  public void thatTheReservationIsForTotalReservationPrice(Money reservationPrice) {
    createReservationDto.setReservationPrice(reservationPrice);
  }

  @And("that the reservation has a cancellation policy {cancellationPolicy}")
  public void thatTheReservationHasACancellationPolicyCancellationPolicy(
      CancellationPolicy cancellationPolicy) {
    createReservationDto.setCancellationPolicy(cancellationPolicy);
  }

  @When("the customer cancels the reservation on {date}")
  public void theCustomerCancelsTheReservationOnReservationCancellationTime(
      LocalDateTime cancellationDate) {
    Reservation reservation = new Reservation(createReservationDto);
    reservation.setReservationId(reservationMockData.randomReservationId());
    configureMocks(reservation);
    reservationMockData.simulateClock(cancellationDate);
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
}
