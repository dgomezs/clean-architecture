package com.acme.reservation.application.usecases.customer_reservation;

import com.acme.reservation.application.event.ReservationCreatedEvent;
import com.acme.reservation.application.event.ReservationEventPublisher;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreateReservationUseCaseImpl implements CreateReservationUseCase {

  private final ReservationRepository reservationRepository;
  private final ReservationEventPublisher eventBus;

  @Transactional
  public Mono<Reservation> createReservation(CreateReservationDto createReservationDto) {
    Reservation reservation = new Reservation(createReservationDto);
    return reservationRepository
        .createReservation(reservation)
        .flatMap(this.reservationRepository::getReservationById)
        .doOnSuccess(r -> eventBus.publish(new ReservationCreatedEvent(r)));
  }
}
