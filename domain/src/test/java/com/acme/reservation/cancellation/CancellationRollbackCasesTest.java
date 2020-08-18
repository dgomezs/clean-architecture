package com.acme.reservation.cancellation;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.application.usecases.cancellation.customer.CancelReservationAsCustomerUseCase;
import com.acme.reservation.entity.Reservation;
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
public class CancellationRollbackCasesTest {

  @Autowired private ReservationMockData reservationMockData;
  @Autowired private ReservationVerificationRules reservationVerificationRules;
  @Autowired private CancelReservationAsCustomerUseCase cancelReservationAsCustomerUseCase;

  @Before
  public void cleanUpMocks() {
    reservationMockData.cleanUpMocks();
  }

  @Test
  public void ensureTransactionIsRollbackIfFinanceGatewayFailsButPersistenceSuccedeeds() {
    MockTransaction mockTransaction = new MockTransaction();
    Reservation reservation = reservationMockData.getRandomReservation();
    reservationMockData.configureTransaction(mockTransaction);
    reservationMockData.simulateFinanceGatewayFails(reservation);
    reservationMockData.updateStatusSucceeds(reservation);

    Mono<RefundBreakdown> refundBreakdownMono =
        cancelReservationAsCustomerUseCase.cancelAsCustomer(reservation.getReservationId());

    StepVerifier.create(refundBreakdownMono)
        .expectErrorSatisfies(
            e -> {
              Assert.assertEquals(e.getClass(), IllegalAccessError.class);
              reservationVerificationRules.verifyTransactionWasRolledBack(mockTransaction);
              reservationVerificationRules.verifyStatusUpdateWasCalled(reservation);
            })
        .verify();
  }

  @Test
  public void ensureTransactionIsRollbackIfFinanceGatewaySucceedsButPersistenceFails() {
    MockTransaction mockTransaction = new MockTransaction();
    Reservation reservation = reservationMockData.getRandomReservation();
    reservationMockData.configureTransaction(mockTransaction);
    reservationMockData.simulateFinanceGatewaySucceeds(reservation);
    reservationMockData.simulateUpdateStatusFails(reservation);

    Mono<RefundBreakdown> refundBreakdownMono =
        cancelReservationAsCustomerUseCase.cancelAsCustomer(reservation.getReservationId());

    StepVerifier.create(refundBreakdownMono)
        .expectErrorSatisfies(
            e -> {
              Assert.assertEquals(e.getClass(), IllegalAccessError.class);
              reservationVerificationRules.verifyTransactionWasRolledBack(mockTransaction);
              reservationVerificationRules.verifyStatusUpdateWasCalled(reservation);
            })
        .verify();
  }
}
