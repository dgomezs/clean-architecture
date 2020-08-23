package com.acme.reservation.cancellation.customer;

import com.acme.reservation.ReservationTestConfig;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.helpers.MockTransaction;
import com.acme.reservation.helpers.ReservationMockData;
import com.acme.reservation.helpers.ReservationVerificationRules;
import org.junit.Assert;
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
public class CancellationErrorsCasesTest {

  @Autowired private ReservationMockData reservationMockData;
  @Autowired private ReservationVerificationRules reservationVerificationRules;
  @Autowired private CancelReservationAsCustomerUseCase cancelReservationAsCustomerUseCase;

  @Before
  public void cleanUpMocks() {
    reservationMockData.cleanUpMocks();
    reservationMockData.simulateCancellationDateInThePast(); // makes sure there is a 100% refund
  }

  @Test
  public void ensureTransactionIsRollbackIfFinanceGatewayFailsButPersistenceSucceeds() {
    MockTransaction mockTransaction = new MockTransaction();
    Reservation reservation = reservationMockData.getFlexReservation();
    reservationMockData.configureTransaction(mockTransaction);
    reservationMockData.simulateFinanceGatewayFails(reservation);
    reservationMockData.updateStatusSucceeds(reservation);
    cancelReservationAndVerifyAssertions(mockTransaction, reservation);
  }

  @Test
  public void ensureTransactionIsRollbackIfFinanceGatewaySucceedsButPersistenceFails() {
    MockTransaction mockTransaction = new MockTransaction();
    Reservation reservation = reservationMockData.getFlexReservation();
    reservationMockData.configureTransaction(mockTransaction);
    reservationMockData.simulateFinanceGatewaySucceeds(reservation);
    reservationMockData.simulateUpdateStatusFails(reservation);

    cancelReservationAndVerifyAssertions(mockTransaction, reservation);
  }

  private void cancelReservationAndVerifyAssertions(
      MockTransaction mockTransaction, Reservation reservation) {
    Mono<RefundBreakdown> refundBreakdownMono =
        cancelReservationAsCustomerUseCase.cancelAsCustomer(reservation.getReservationId());

    StepVerifier.create(refundBreakdownMono)
        .expectErrorSatisfies(
            e -> {
              Assert.assertEquals(e.getClass(), IllegalAccessError.class);
              reservationVerificationRules.verifyTransactionWasRolledBack(mockTransaction);
              reservationVerificationRules.verifyStatusUpdateWasCalled(reservation);
              reservationVerificationRules.verifyNoEventsWerePublished();
            })
        .verify();
  }
}
