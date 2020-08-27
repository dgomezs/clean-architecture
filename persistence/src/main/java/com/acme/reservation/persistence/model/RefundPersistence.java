package com.acme.reservation.persistence.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("refund")
public class RefundPersistence {
  @Id private Long id;
  private String reservationId;
  private BigDecimal refundAmount;
  private LocalDateTime creationTimestamp;
}
