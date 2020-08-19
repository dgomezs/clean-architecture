package com.acme.reservation.entity;

import lombok.Data;

@Data
public class Customer {
  private Long id;
  private String firstName;
  private String lastName;
  private Email email;
}
