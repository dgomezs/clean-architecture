package com.acme.reservation.cancellation;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.cancellation.helpers.MockTransaction;
import com.acme.reservation.cancellation.helpers.ReservationMockData;
import com.acme.reservation.cancellation.helpers.ReservationVerificationRules;
import com.acme.reservation.entity.Reservation;
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
  }

  @Test
  public void ensureReservationIsCancelled() {
    MockTransaction mockTransaction = new MockTransaction();
    Reservation reservation = reservationMockData.getRandomReservation();
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
              reservationVerificationRules.verifyStatusUpdateWasCalled(reservation);
              reservationVerificationRules.verifyCancellationEventWasPublished(reservation);
            })
        .verifyComplete();
  }
}
