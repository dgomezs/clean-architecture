package com.acme.reservation.persistence.adapters;

import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.persistence.model.ReservationPersistence;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(
    componentModel = "spring",
    uses = {CustomerAdapter.class, DestinationAdapter.class})
public interface ReservationAdapter {

  @Mapping(
      target = "cancellationTimestamp",
      source = "cancellationTimestamp",
      qualifiedByName = "unwrap")
  ReservationPersistence toReservationPersistence(Reservation reservation);

  default String toReservationId(ReservationId reservationId) {
    return reservationId.getKey();
  }

  @Named("unwrap")
  default <T> T unwrap(Optional<T> optional) {
    return optional.orElse(null);
  }
}
