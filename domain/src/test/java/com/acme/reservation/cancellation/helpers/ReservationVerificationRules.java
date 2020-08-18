package com.acme.reservation.cancellation.helpers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.cancellation.helpers.matchers.ReservationWasCancelledEvent;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.gateway.FinanceGateway;
import lombok.AllArgsConstructor;
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
        .publish(argThat(new ReservationWasCancelledEvent(reservation)));
  }
}
