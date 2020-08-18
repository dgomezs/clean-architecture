package com.acme.reservation.application.event;

import com.acme.reservation.entity.ReservationId;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class ReservationCancelled extends ReservationEvent {
  public ReservationCancelled(ReservationId reservationId) {
    super(reservationId);
  }
}
