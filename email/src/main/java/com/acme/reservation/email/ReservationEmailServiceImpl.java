package com.acme.reservation.email;

import com.acme.reservation.application.event.ReservationCancelledEvent;
import com.acme.reservation.application.event.ReservationCreatedEvent;
import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;
import java.text.NumberFormat;
import javax.annotation.PostConstruct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.ReplayProcessor;
import reactor.core.scheduler.Scheduler;

public class ReservationEmailServiceImpl implements ReservationEmailService {

  private final ReplayProcessor<ReservationEvent> eventBus;
  private final Scheduler scheduler;
  private final EmailSender emailSender;
  private final NumberFormat numberFormat;

  public ReservationEmailServiceImpl(
      ReplayProcessor<ReservationEvent> eventBus, EmailSender emailSender, Scheduler scheduler) {
    this.eventBus = eventBus;
    this.scheduler = scheduler;
    this.emailSender = emailSender;
    this.numberFormat = NumberFormat.getNumberInstance();
    this.numberFormat.setGroupingUsed(true);
    this.numberFormat.setMaximumFractionDigits(2);
    this.numberFormat.setMinimumFractionDigits(2);
  }

  @PostConstruct
  public void configureSubscribers() {

    Flux<ReservationEvent> createdEvents =
        eventBus.filter(e -> e instanceof ReservationCreatedEvent);
    Flux<ReservationEvent> cancelledEvents =
        eventBus.filter(e -> e instanceof ReservationCancelledEvent);

    createdEvents
        .subscribeOn(this.scheduler)
        .subscribe(e -> this.sendReservationCreatedEmail(e.getReservation()));
    cancelledEvents
        .subscribeOn(this.scheduler)
        .map(e -> (ReservationCancelledEvent) e)
        .subscribe(
            e -> this.sendReservationCancelledEmail(e.getReservation(), e.getRefundBreakdown()));
  }

  public void sendReservationCreatedEmail(Reservation reservation) {
    this.emailSender.sendEmail(reservation.getCustomer().getEmail(), "Reservation created");
  }

  public void sendReservationCancelledEmail(
      Reservation reservation, RefundBreakdown refundBreakdown) {
    if (refundBreakdown.isThereMoneyToRefundToCustomer()) {
      this.emailSender.sendEmail(
          reservation.getCustomer().getEmail(),
          "Reservation cancelled with refund " + refundFormat(refundBreakdown));
    } else {
      this.emailSender.sendEmail(
          reservation.getCustomer().getEmail(), "Reservation cancelled without refund ");
    }
  }

  private String refundFormat(RefundBreakdown refundBreakdown) {
    return numberFormat.format(refundBreakdown.getAmountToRefund().getPrice().doubleValue());
  }
}
