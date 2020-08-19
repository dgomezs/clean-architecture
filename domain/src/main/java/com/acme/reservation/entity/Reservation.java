package com.acme.reservation.entity;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.dto.CreateReservationDto;
import com.acme.reservation.dto.LoadCancellationDto;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.time.Instant;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class Reservation extends SelfValidating<Reservation> {

  @Getter @Setter private ReservationId reservationId;

  @NotNull(message = "the reservation needs to have a start date")
  @Getter
  private final LocalDateTime startDate;

  @NotNull(message = "the reservation needs to have an end date")
  @Getter
  private final LocalDateTime endDate;

  @NotNull(message = "the reservation needs to have a price")
  @Getter
  private final Money reservationPrice;

  @NotNull(message = "the reservation needs to have a cancellation policy")
  @Getter
  private final CancellationPolicy cancellationPolicy;

  @NotNull(message = "the reservation needs to have a customer")
  @Getter
  private final Customer customer;

  @NotNull(message = "the reservation needs to have a status")
  @Getter
  private ReservationStatus reservationStatus;

  @Getter private RefundBreakdown currentRefund;
  @Getter private Instant cancellationTimestamp;

  public Reservation(CreateReservationDto dto) {
    this.reservationStatus = ReservationStatus.CREATED;
    this.reservationPrice = dto.getReservationPrice();
    this.cancellationPolicy = dto.getCancellationPolicy();
    this.startDate = dto.getStartDate();
    this.endDate = dto.getEndDate();
    this.customer = dto.getCustomer();
    this.validateSelf();
  }

  public Reservation(LoadCancellationDto dto) {
    this.reservationId = dto.getReservationId();
    this.reservationStatus = dto.getReservationStatus();
    this.reservationPrice = dto.getReservationPrice();
    this.cancellationPolicy = dto.getCancellationPolicy();
    this.cancellationTimestamp = dto.getCancellationTimestamp();
    this.startDate = dto.getStartDate();
    this.endDate = dto.getEndDate();
    this.customer = dto.getCustomer();
    this.validateSelf();
  }

  public void cancel(RefundBreakdown refundBreakdown) {
    this.reservationStatus = ReservationStatus.CANCELLED;
    this.currentRefund = refundBreakdown;
    this.cancellationTimestamp = Instant.now();
    this.validateSelf();
  }

  public Money getTotalRefundableMoneyToCustomer() {
    // for now we assume that the total price of the reservation can be refunded
    return reservationPrice;
  }
}
