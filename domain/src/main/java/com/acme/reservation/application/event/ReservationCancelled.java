package com.acme.reservation.application.event;

import com.acme.reservation.entity.Reservation;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ReservationCancelled extends ReservationEvent {
  public ReservationCancelled(Reservation reservation) {
    super(reservation);
  }
}
