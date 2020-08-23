package com.acme.reservation.application.usecases.customer_reservation;

import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.entity.Reservation;
import reactor.core.publisher.Mono;

public interface CreateReservationUseCase {

  Mono<Reservation> createReservation(CreateReservationDto createReservationDto);
}
