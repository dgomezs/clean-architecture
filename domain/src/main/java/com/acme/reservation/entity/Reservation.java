package com.acme.reservation.entity;

import com.acme.reservation.application.repository.LoadReservationDto;
import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class Reservation extends SelfValidating<Reservation> {

  @Getter @Setter private ReservationId reservationId;

  @NotNull(message = "the reservation needs to have a start date")
  @Getter
  private final LocalDateTime startDate;

  @NotNull(message = "the reservation needs to have a destination")
  @Getter
  private final Destination destination;

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

  private RefundBreakdown currentRefund;
  private Instant cancellationTimestamp;

  public Reservation(CreateReservationDto dto) {
    this.reservationStatus = ReservationStatus.CREATED;
    this.reservationPrice = dto.getReservationPrice();
    this.cancellationPolicy = dto.getCancellationPolicy();
    this.startDate = dto.getStartDate();
    this.endDate = dto.getEndDate();
    this.customer = dto.getCustomer();
    this.destination = dto.getDestination();
    this.validateSelf();
  }

  public Reservation(LoadReservationDto dto) {
    this.reservationId = dto.getReservationId();
    this.reservationStatus = dto.getReservationStatus();
    this.reservationPrice = dto.getReservationPrice();
    this.cancellationPolicy = dto.getCancellationPolicy();
    this.cancellationTimestamp = dto.getCancellationTimestamp();
    this.startDate = dto.getStartDate();
    this.endDate = dto.getEndDate();
    this.customer = dto.getCustomer();
    this.destination = dto.getDestination();
    this.validateSelf();
    this.validateStatus();
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

  public Optional<RefundBreakdown> getCurrentRefund() {
    return Optional.ofNullable(currentRefund);
  }

  public Optional<Instant> getCancellationTimestamp() {
    return Optional.ofNullable(cancellationTimestamp);
  }

  private void validateStatus() {
    checkCancellationRules();
  }

  private void checkCancellationRules() {
    if (ReservationStatus.CANCELLED.equals(this.reservationStatus)
        && getCancellationTimestamp().isEmpty()) {
      throw new IllegalStateException(
          String.format(
              "Reservation %s is status cancelled without a cancellation timestamp",
              reservationId.getKey()));
    }
  }
}
