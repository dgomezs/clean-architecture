package com.acme.reservation.entity;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class Money {
  BigDecimal price;
}
