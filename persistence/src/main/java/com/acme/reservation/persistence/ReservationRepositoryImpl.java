package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import reactor.core.publisher.Mono;

public class ReservationRepositoryImpl implements ReservationRepository {

  public Mono<ReservationId> createReservation(CreateReservationDto createReservationDto) {
    return null;
  }

  public Mono<Reservation> getReservationById(ReservationId reservationId) {
    return null;
  }

  public Mono<Void> updateStatus(Reservation reservation) {
    return null;
  }
}
