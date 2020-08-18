package com.acme.reservation.cancellation.helpers.matchers;

import com.acme.reservation.application.event.ReservationCancelled;
import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.entity.Reservation;
import org.mockito.ArgumentMatcher;

public class ReservationWasCancelledEvent implements ArgumentMatcher<ReservationEvent> {

  private final Reservation reservation;

  public ReservationWasCancelledEvent(Reservation reservation) {
    this.reservation = reservation;
  }

  @Override
  public boolean matches(ReservationEvent event) {
    return event.getClass().equals(ReservationCancelled.class)
        && event.getReservation().equals(reservation);
  }
}
