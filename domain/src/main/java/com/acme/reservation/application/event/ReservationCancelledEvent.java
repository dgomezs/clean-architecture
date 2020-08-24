package com.acme.reservation.application.event;

import com.acme.reservation.entity.Reservation;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ReservationCancelledEvent extends ReservationEvent {
  public ReservationCancelledEvent(Reservation reservation) {
    super(reservation);
  }
}
