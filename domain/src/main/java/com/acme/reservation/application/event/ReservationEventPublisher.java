package com.acme.reservation.application.event;

public interface ReservationEventPublisher {

  public void publish(ReservationEvent event);
}
