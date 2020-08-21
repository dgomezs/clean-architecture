package com.acme.reservation.application.repository;

import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import reactor.core.publisher.Mono;

public interface ReservationRepository {

  Mono<ReservationId> createReservation(CreateReservationDto createReservationDto);

  Mono<Reservation> getReservationById(ReservationId reservationId);

  Mono<Void> updateStatus(Reservation reservation);
}
