package com.acme.reservation.cancellation.helpers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.cancellation.helpers.matchers.ReservationWasCancelledMatcher;
import com.acme.reservation.cancellation.helpers.matchers.ReservationWasCancelledMatcherEvent;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.gateway.FinanceGateway;
import lombok.AllArgsConstructor;
import org.junit.Assert;
import org.mockito.Mockito;
import org.springframework.transaction.ReactiveTransactionManager;

@AllArgsConstructor
public class ReservationVerificationRules {
  private final ReservationRepository reservationRepository;
  private final ReactiveTransactionManager reactiveTransactionManager;
  private final FinanceGateway financeGateway;
  private final ReservationEventPublisher eventPublisher;

  public void verifyTransactionWasRolledBack(MockTransaction mockTransaction) {
    Mockito.verify(this.reactiveTransactionManager, times(1)).rollback(mockTransaction);
    Mockito.verify(this.reactiveTransactionManager, never()).commit(mockTransaction);
  }

  public void verifyTransactionWasCommitted(MockTransaction mockTransaction) {
    Mockito.verify(this.reactiveTransactionManager, never()).rollback(mockTransaction);
    Mockito.verify(this.reactiveTransactionManager, times(1)).commit(mockTransaction);
  }

  public void verifyStatusUpdateWasCalled(Reservation reservation) {
    Mockito.verify(this.reservationRepository, times(1)).updateStatus(reservation);
  }

  public void verifyNoEventsWerePublished() {
    Mockito.verify(this.eventPublisher, never()).publish(any());
  }

  public void verifyCancellationEventWasPublished(Reservation reservation) {
    Mockito.verify(this.eventPublisher, times(1))
        .publish(argThat(new ReservationWasCancelledMatcherEvent(reservation)));
  }

  public void verifyReservationWasRefunded(
      RefundBreakdown refundBreakdown, Reservation reservation) {
    if (refundBreakdown.isThereMoneyToRefundToCustomer()) {
      Mockito.verify(this.financeGateway, times(1)).refundReservation(refundBreakdown, reservation);
    }
  }

  public void verifyReservationWasCancelled(
      Reservation reservation, RefundBreakdown refundBreakdown) {
    Mockito.verify(this.reservationRepository, times(1))
        .updateStatus(argThat(new ReservationWasCancelledMatcher(reservation, refundBreakdown)));
  }

  public void verifyAllMoneyWasRefunded(RefundBreakdown refundBreakdown, Reservation reservation) {
    Assert.assertTrue(refundBreakdown.isThereMoneyToRefundToCustomer());
    Assert.assertEquals(
        refundBreakdown.getAmountToRefund(), reservation.getTotalRefundableMoneyToCustomer());
    Mockito.verify(this.financeGateway, times(1)).refundReservation(refundBreakdown, reservation);
  }
}
