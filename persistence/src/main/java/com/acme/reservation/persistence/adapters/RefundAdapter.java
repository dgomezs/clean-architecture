package com.acme.reservation.persistence.adapters;

import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.persistence.model.RefundPersistence;
import java.math.BigDecimal;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefundAdapter {

  @Mapping(target = "reservationId", source = "reservationId")
  @Mapping(target = "refundAmount", source = "currentRefund")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "creationTimestamp", ignore = true)
  RefundPersistence toRefundPersistence(Reservation reservation);

  default String map(ReservationId value) {
    return value.getKey();
  }

  default BigDecimal map(Optional<RefundBreakdown> value) {
    return value.map(v -> v.getAmountToRefund().getPrice()).orElse(null);
  }
}
