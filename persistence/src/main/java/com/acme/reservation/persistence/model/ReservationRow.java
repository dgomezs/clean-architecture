package com.acme.reservation.persistence.model;

import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
public class ReservationRow {
  private Long customerId;
  private String firstName;
  private String lastName;
  private String email;
  private String reservationId;
  private CancellationPolicy cancellationPolicy;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private ReservationStatus status;
  private BigDecimal price;
  // TODO replace this with instant once supported by the codecs
  private LocalDateTime creationTimestamp;
  private LocalDateTime cancellationTimestamp;

  @Column("name")
  private String reservationName;

  private Long destinationId;
  private String timeZone;
}
