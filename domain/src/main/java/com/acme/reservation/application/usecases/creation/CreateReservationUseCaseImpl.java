package com.acme.reservation.application.usecases.creation;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class CreateReservationUseCaseImpl implements CreateReservationUseCase {

  private final ReservationRepository reservationRepository;

  public Mono<Reservation> createReservation(CreateReservationDto createReservationDto) {
    return this.reservationRepository
        .createReservation(createReservationDto)
        .flatMap(this.reservationRepository::getReservationById);
  }
}
