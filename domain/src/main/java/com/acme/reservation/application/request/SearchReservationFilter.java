package com.acme.reservation.application.request;

import com.acme.reservation.entity.ReservationStatus;
import java.util.List;
import lombok.Data;

@Data
public class SearchReservationFilter {
  private Long customerId;
  private List<ReservationStatus> statusList;
}
