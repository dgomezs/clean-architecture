package com.acme.reservation.application.request;

import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateReservationDto {
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final Money price;
  private final CancellationPolicy cancellationPolicy;
  private final Customer customer;
  private final Destination destination;
}
