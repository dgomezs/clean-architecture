package com.acme.reservation.dto;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LoadCancellationDto {
  private ReservationId reservationId;

  private LocalDateTime startDate;
  private LocalDateTime endDate;

  private ReservationStatus reservationStatus;
  private RefundBreakdown currentRefund;
  private Money reservationPrice;
  private CancellationPolicy cancellationPolicy;
  private Instant cancellationTimestamp;
  private Customer customer;
}
