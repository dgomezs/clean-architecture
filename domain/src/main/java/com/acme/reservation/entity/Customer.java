package com.acme.reservation.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Customer extends SelfValidating<Customer> {
  private final Long id;
  private final String firstName;
  private final String lastName;
  private final Email email;
}
