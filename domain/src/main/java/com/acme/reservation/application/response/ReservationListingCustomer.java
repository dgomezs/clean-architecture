package com.acme.reservation.application.response;

import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReservationListingCustomer {

  private final String reservationId;

  private final LocalDateTime startDate;

  private final Destination destination;

  private final LocalDateTime endDate;

  private final Money price;

  private final CancellationPolicy cancellationPolicy;

  private final ReservationStatus status;

  private final Instant cancellationTimestamp;

  public Optional<Instant> getCancellationTimestamp() {
    return Optional.ofNullable(cancellationTimestamp);
  }
}
