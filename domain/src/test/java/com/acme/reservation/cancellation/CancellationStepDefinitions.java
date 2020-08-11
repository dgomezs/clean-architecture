package com.acme.reservation.cancellation;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CancellationStepDefinitions {

  @ParameterType(".*")
  public ZonedDateTime date(String date) {
    return LocalDate.parse(date).atStartOfDay(ZoneId.of("UTC"));
  }

  @Given("that the customer has a reservation starting on {date} and ending on {date}")
  public void initReservation(
      ZonedDateTime reservationStartDate, ZonedDateTime reservationEndDate) {
    log.info("tested " + reservationEndDate.toString());
  }

  @And("that the reservation is for <TotalReservationPrice>")
  public void thatTheReservationIsForTotalReservationPrice() {}

  @And("that the reservation has a cancellation policy <CancellationPolicy>")
  public void thatTheReservationHasACancellationPolicyCancellationPolicy() {}

  @When("the customer cancels the reservation on <ReservationCancellationTime>")
  public void theCustomerCancelsTheReservationOnReservationCancellationTime() {}

  @Then("the customer should receive <TotalRefundedMoney>")
  public void theCustomerShouldReceiveTotalRefundedMoney() {}
}
