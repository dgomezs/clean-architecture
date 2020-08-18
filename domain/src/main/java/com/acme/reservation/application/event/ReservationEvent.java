package com.acme.reservation.application.event;

import com.acme.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class ReservationEvent {

  @Getter
  protected final Reservation reservation;
}
