package com.acme.reservation.cancellation.helpers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import com.acme.reservation.gateway.FinanceGateway;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
  private final Clock clock;

  public Reservation getFlexReservation() {
    Reservation reservation = createRandomFlexReservation();
    mockGetReservation(reservation);
    return reservation;
  }

  public void mockGetReservation(Reservation reservation) {
    Mockito.when(this.reservationRepository.getReservationById(reservation.getReservationId()))
        .thenReturn(Mono.just(reservation));
  }

  public void configureTransaction(MockTransaction mockTransaction) {

    Mockito.when(this.reactiveTransactionManager.getReactiveTransaction(any()))
        .thenReturn(Mono.just(mockTransaction));
    Mockito.when(this.reactiveTransactionManager.commit(mockTransaction)).thenReturn(getEmpty());
    Mockito.when(this.reactiveTransactionManager.rollback(mockTransaction)).thenReturn(getEmpty());
  }

  public ReservationId randomReservationId() {
    return new ReservationId(RandomStringUtils.randomAlphabetic(5));
  }

  private Mono<Void> getEmpty() {
    return Mono.empty();
  }

  private Reservation createRandomFlexReservation() {
    CreateReservationDto createReservationDto = new CreateReservationDto();
    createReservationDto.setReservationPrice(new Money(BigDecimal.valueOf(100)));
    createReservationDto.setCancellationPolicy(CancellationPolicy.FLEX);
    createReservationDto.setCustomer(new Customer());
    createReservationDto.setStartDate(LocalDateTime.now());
    createReservationDto.setEndDate(LocalDateTime.now().plusDays(5));
    Reservation reservation = new Reservation(createReservationDto);
    reservation.setReservationId(randomReservationId());
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
    Mockito.reset(clock);
  }

  public void simulateClock(LocalDateTime cancellationDate, ZoneId zoneId) {
    Mockito.when(clock.instant()).thenReturn(cancellationDate.atZone(zoneId).toInstant());
  }

  public void simulateCancellationDateInThePast() {
    Mockito.when(clock.instant())
        .thenReturn(LocalDateTime.now().minusDays(5).toInstant(ZoneOffset.UTC));
  }
}
