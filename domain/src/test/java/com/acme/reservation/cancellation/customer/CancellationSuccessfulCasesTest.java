package com.acme.reservation.cancellation.customer;

import com.acme.reservation.ReservationTestConfig;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.helpers.MockTransaction;
import com.acme.reservation.helpers.ReservationMockData;
import com.acme.reservation.helpers.ReservationVerificationRules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservationTestConfig.class)
public class CancellationSuccessfulCasesTest {

  @Autowired private ReservationMockData reservationMockData;
  @Autowired private ReservationVerificationRules reservationVerificationRules;
  @Autowired private CancelReservationAsCustomerUseCase cancelReservationAsCustomerUseCase;

  @Before
  public void cleanUpMocks() {
    reservationMockData.cleanUpMocks();
    reservationMockData.simulateCancellationDateInThePast();
  }

  @Test
  public void ensureReservationIsCancelled() {
    MockTransaction mockTransaction = new MockTransaction();
    Reservation reservation = reservationMockData.getExistingFlexReservation();
    reservationMockData.configureTransaction(mockTransaction);
    reservationMockData.simulateFinanceGatewaySucceeds(reservation);
    reservationMockData.updateStatusSucceeds(reservation);

    cancelReservationAndVerifyAssertions(mockTransaction, reservation);
  }

  private void cancelReservationAndVerifyAssertions(
      MockTransaction mockTransaction, Reservation reservation) {
    Mono<RefundBreakdown> refundBreakdownMono =
        cancelReservationAsCustomerUseCase.cancelAsCustomer(reservation.getReservationId());

    StepVerifier.create(refundBreakdownMono)
        .assertNext(
            refundBreakdown -> {
              reservationVerificationRules.verifyTransactionWasCommitted(mockTransaction);
              reservationVerificationRules.verifyReservationWasCancelled(
                  reservation, refundBreakdown);
              reservationVerificationRules.verifyReservationWasRefunded(
                  refundBreakdown, reservation);
              reservationVerificationRules.verifyCancellationEventWasPublished(reservation);
            })
        .verifyComplete();
  }
}
