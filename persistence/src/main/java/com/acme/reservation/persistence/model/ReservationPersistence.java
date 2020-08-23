package com.acme.reservation.persistence.model;

import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("reservation")
public class ReservationPersistence implements Persistable<String> {

  @Id private String reservationId;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private BigDecimal price;

  private CancellationPolicy cancellationPolicy;

  private Long customerId;
  private Long destinationId;

  private ReservationStatus status;

  private LocalDateTime cancellationTimestamp;
  private LocalDateTime creationTimestamp;

  @Transient private boolean newReservation;

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
