package com.acme.reservation.persistence.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("customer")
public class CustomerPersistence {
  @Id private Long id;
  private String firstName;
  private String lastName;
  private String email;
}
