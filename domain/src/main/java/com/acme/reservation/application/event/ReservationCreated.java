package com.acme.reservation.application.event;

import com.acme.reservation.entity.Reservation;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ReservationCreated extends ReservationEvent {
  public ReservationCreated(Reservation reservation) {
    super(reservation);
  }
}
