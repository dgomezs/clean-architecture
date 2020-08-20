package com.acme.reservation.application.request;

import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CreateReservationDto {
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Money reservationPrice;
  private CancellationPolicy cancellationPolicy;
  private Customer customer;
  private Destination destination;
}
