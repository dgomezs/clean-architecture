package com.acme.reservation.persistence.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DestinationPersistence {
  private Long id;
  private String name;
  private String timeZone;
}
