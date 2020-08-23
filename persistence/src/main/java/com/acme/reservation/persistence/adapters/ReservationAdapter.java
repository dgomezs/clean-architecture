package com.acme.reservation.persistence.adapters;

import com.acme.reservation.application.repository.LoadReservationDto;
import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Email;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.persistence.model.ReservationPersistence;
import com.acme.reservation.persistence.model.ReservationRow;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
  ReservationPersistence toReservationPersistence(Reservation reservation);

  @Mapping(target = "customer", source = ".")
  @Mapping(target = "destination", source = ".")
  LoadReservationDto toReservation(ReservationRow reservationRow);

  @Mapping(target = "id", source = "customerId")
  Customer toCustomer(ReservationRow reservationRow);

  @Mapping(target = "name", source = "reservationName")
  Destination toDestination(ReservationRow reservationRow);

  default ZoneId toZoneId(String timeZone) {
    return ZoneId.of(timeZone);
  }

  default ReservationId toReservationId(String reservationId) {
    return new ReservationId(reservationId);
  }

  default LocalDateTime toLocalDateTime(Optional<Instant> value) {
    return value.map(this::toLocalDateTime).orElse(null);
  }

  default Instant toInstant(LocalDateTime value) {
    return Optional.ofNullable(value).map(t -> t.toInstant(ZoneOffset.UTC)).orElse(null);
  }

  default LocalDateTime toLocalDateTime(Instant value) {
    return LocalDateTime.ofInstant(value, ZoneId.of("UTC"));
  }

  default Money toMoney(BigDecimal value) {
    return new Money(value);
  }

  default String toReservationId(ReservationId reservationId) {
    return reservationId.getKey();
  }

  default Email map(String value) {
    return new Email(value);
  }

  default BigDecimal toPrice(Money money) {
    return money.getPrice();
  }

  @Named("unwrap")
  default <T> T unwrap(Optional<T> optional) {
    return optional.orElse(null);
  }

  ReservationListingCustomer toReservationListingCustomer(ReservationRow reservationRow);

  @Mapping(target = "reservationListingCustomer", source = ".")
  ReservationListingAcmeTeam toReservationListingAcmeTeam(ReservationRow reservationRow);
}
