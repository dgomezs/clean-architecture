package com.acme.reservation.rest.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestDestination {
  private Long id;
  private String name;
  private String timeZone;
}
