package com.acme.reservation.helpers.matchers;

import com.acme.reservation.application.event.ReservationCancelledEvent;
import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.entity.Reservation;
import org.mockito.ArgumentMatcher;

public class ReservationWasCancelledMatcherEvent implements ArgumentMatcher<ReservationEvent> {

  private final Reservation reservation;

  public ReservationWasCancelledMatcherEvent(Reservation reservation) {
    this.reservation = reservation;
  }

  @Override
  public boolean matches(ReservationEvent event) {
    return event.getClass().equals(ReservationCancelledEvent.class)
        && event.getReservation().equals(reservation);
  }
}
