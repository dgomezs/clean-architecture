package com.acme.reservation.persistence.model;

import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("reservation")
public class ReservationPersistence implements Persistable<String> {

  @Id
  private String reservationId;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private BigDecimal price;

  private CancellationPolicy cancellationPolicy;

  private Long customerId;
  private Long destinationId;

  private ReservationStatus reservationStatus;

  private Instant cancellationTimestamp;
  @Transient
  @Setter
  private boolean newReservation = false;


  @Override
  public String getId() {
    return reservationId;
  }

  @Override
  @Transient
  public boolean isNew() {
    return newReservation;
  }
}
