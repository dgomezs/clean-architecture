package com.acme.reservation.application.exception;

import java.util.NoSuchElementException;

public class ReservationNotFoundException extends NoSuchElementException {

  public ReservationNotFoundException(String s) {
    super(s);
  }
}
