package com.acme.reservation.rest.response;

import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import com.acme.reservation.rest.adapter.FormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestReservationListingAcmeTeam {
  @JsonFormat(pattern = FormatConstants.DATE_TIME_FORMAT)
  private LocalDateTime startDate;

  private RestDestination destination;

  @JsonFormat(pattern = FormatConstants.DATE_TIME_FORMAT)
  private LocalDateTime endDate;

  private BigDecimal price;

  private CancellationPolicy cancellationPolicy;

  private ReservationStatus status;

  private Instant cancellationTimestamp;
}
