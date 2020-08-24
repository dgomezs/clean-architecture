package com.acme.reservation.helpers.matchers;

import com.acme.reservation.application.event.ReservationCreatedEvent;
import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.entity.Reservation;
import org.mockito.ArgumentMatcher;

public class ReservationWasCreatedMatcherEvent implements ArgumentMatcher<ReservationEvent> {

  private final Reservation reservation;

  public ReservationWasCreatedMatcherEvent(Reservation reservation) {
    this.reservation = reservation;
  }

  @Override
  public boolean matches(ReservationEvent event) {
    return event.getClass().equals(ReservationCreatedEvent.class)
        && event.getReservation().equals(reservation);
  }
}
