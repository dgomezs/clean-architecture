package com.acme.reservation.app;

import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.application.event.ReservationEventPublisher;
import reactor.core.publisher.ReplayProcessor;

public class EventBusImpl implements ReservationEventPublisher {

  private final ReplayProcessor<ReservationEvent> eventBus;

  public EventBusImpl(ReplayProcessor<ReservationEvent> eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void publish(ReservationEvent event) {
    this.eventBus.onNext(event);
  }
}
