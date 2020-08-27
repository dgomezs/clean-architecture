package com.acme.reservation.rest.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestCustomer {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
}
