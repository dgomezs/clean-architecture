package com.acme.reservation.persistence.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("destination")
public class DestinationPersistence {
  @Id private Long id;
  private String name;
  private String timeZone;
}
