package com.acme.reservation.cancellation.helpers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.gateway.FinanceGateway;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.mockito.Mockito;
import org.springframework.transaction.ReactiveTransactionManager;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ReservationMockData {
  private final ReservationRepository reservationRepository;
  private final ReactiveTransactionManager reactiveTransactionManager;
  private final FinanceGateway financeGateway;
  private final ReservationEventPublisher eventPublisher;

  public Reservation getRandomReservation() {
    Reservation reservation = createRandomReservation();
    Mockito.when(this.reservationRepository.getReservationById(reservation.getReservationId()))
        .thenReturn(Mono.just(reservation));
    return reservation;
  }

  public void configureTransaction(MockTransaction mockTransaction) {

    Mockito.when(this.reactiveTransactionManager.getReactiveTransaction(any()))
        .thenReturn(Mono.just(mockTransaction));
    Mockito.when(this.reactiveTransactionManager.commit(mockTransaction)).thenReturn(getEmpty());
    Mockito.when(this.reactiveTransactionManager.rollback(mockTransaction)).thenReturn(getEmpty());
  }

  private Mono<Void> getEmpty() {
    return Mono.empty();
  }

  private Reservation createRandomReservation() {
    Reservation reservation = new Reservation();
    reservation.setReservationId(new ReservationId(RandomStringUtils.randomAlphabetic(5)));
    return reservation;
  }

  public void simulateFinanceGatewayFails(Reservation reservation) {
    Mockito.when(this.financeGateway.refundReservation(any(), eq(reservation)))
        .thenReturn(Mono.error(new IllegalAccessError()));
  }

  public void simulateUpdateStatusFails(Reservation reservation) {
    Mockito.when(this.reservationRepository.updateStatus(reservation))
        .thenReturn(Mono.error(new IllegalAccessError()));
  }

  public void updateStatusSucceeds(Reservation reservation) {
    Mockito.when(this.reservationRepository.updateStatus(reservation)).thenReturn(getEmpty());
  }

  public void simulateFinanceGatewaySucceeds(Reservation reservation) {
    Mockito.when(this.financeGateway.refundReservation(any(), eq(reservation)))
        .thenReturn(Mono.empty());
  }

  public void cleanUpMocks() {
    Mockito.reset(reactiveTransactionManager);
    Mockito.reset(financeGateway);
    Mockito.reset(reservationRepository);
    Mockito.reset(eventPublisher);
  }
}
