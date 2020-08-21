package com.acme.reservation.persistence.adapters;

import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.persistence.model.ReservationPersistence;
import java.math.BigDecimal;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ReservationAdapter {

  @Mapping(
      target = "cancellationTimestamp",
      source = "cancellationTimestamp",
      qualifiedByName = "unwrap")
  @Mapping(target = "customerId", source = "customer.id")
  @Mapping(target = "destinationId", source = "destination.id")
  @Mapping(target = "price", source = "reservationPrice")
  ReservationPersistence toReservationPersistence(Reservation reservation);

  default String toReservationId(ReservationId reservationId) {
    return reservationId.getKey();
  }

  default BigDecimal toPrice(Money money) {
    return money.getPrice();
  }

  @Named("unwrap")
  default <T> T unwrap(Optional<T> optional) {
    return optional.orElse(null);
  }
}
