package com.acme.reservation.persistence.model;

import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Builder
public class ReservationPersistence {
  @Id private String reservationId;

  private LocalDateTime startDate;

  private DestinationPersistence destination;

  private LocalDateTime endDate;

  private BigDecimal price;

  private CancellationPolicy cancellationPolicy;

  private CustomerPersistence customer;

  private ReservationStatus reservationStatus;

  private Instant cancellationTimestamp;
}
