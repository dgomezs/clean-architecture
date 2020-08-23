package com.acme.reservation.application.request;

import com.acme.reservation.entity.ReservationStatus;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import org.springframework.util.CollectionUtils;

@Data
public class SearchReservationFilter {
  private Long customerId;
  private List<ReservationStatus> statusList;

  public Optional<Long> getCustomerId() {
    return Optional.ofNullable(customerId);
  }

  public boolean hasStatuses() {
    return !CollectionUtils.isEmpty(statusList);
  }
}
