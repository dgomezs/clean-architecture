package com.acme.reservation.cancellation;

import static org.mockito.Mockito.times;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
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
  }

  public void verifyStatusUpdateWasCalled(Reservation reservation) {
    Mockito.verify(this.reservationRepository, times(1)).updateStatus(reservation);
  }
}
