package com.acme.reservation.application.event;

import com.acme.reservation.entity.ReservationId;

public abstract class ReservationEvent {

  protected final ReservationId reservationId;

  public ReservationEvent(ReservationId reservationId) {
    this.reservationId = reservationId;
  }
}
