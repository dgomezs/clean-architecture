package com.acme.reservation.application.repository;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoadReservationDto {
  private final ReservationId reservationId;
  private final Destination destination;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;

  private final ReservationStatus reservationStatus;
  private final RefundBreakdown currentRefund;
  private final Money reservationPrice;
  private final CancellationPolicy cancellationPolicy;
  private final Instant cancellationTimestamp;
  private final Customer customer;
}
