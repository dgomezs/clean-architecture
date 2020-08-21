package com.acme.reservation.application.usecases.creation;

import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.entity.Reservation;
import reactor.core.publisher.Mono;

public interface CreateReservationUseCase {

  Mono<Reservation> createReservation(CreateReservationDto createReservationDto);
}
