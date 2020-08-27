package com.acme.reservation.rest.adapter;

import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Email;
import com.acme.reservation.entity.Money;
import com.acme.reservation.rest.response.RestCustomer;
import com.acme.reservation.rest.response.RestDestination;
import com.acme.reservation.rest.response.RestReservationListingAcmeTeam;
import com.acme.reservation.rest.response.RestReservationListingCustomer;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestReservationAdapter {

  RestReservationListingCustomer toReservationListingCustomer(
      ReservationListingCustomer reservationListingCustomer);

  default LocalDateTime toLocalDateTime(Optional<Instant> value) {
    return value.map(this::toLocalDateTime).orElse(null);
  }

  default LocalDateTime toLocalDateTime(Instant value) {
    return LocalDateTime.ofInstant(value, ZoneId.of("UTC"));
  }

  default Instant toInstant(LocalDateTime value) {
    return Optional.ofNullable(value).map(t -> t.toInstant(ZoneOffset.UTC)).orElse(null);
  }

  default String toZoneId(ZoneId zoneId) {
    return zoneId.getId();
  }

  RestDestination toDestination(Destination destination);

  RestCustomer toCustomer(Customer customer);

  default String fromEmail(Email email) {
    return email.getEmail();
  }

  default BigDecimal toPrice(Money money) {
    return money.getPrice();
  }

  RestReservationListingAcmeTeam toReservationListingAcmeTeam(
      ReservationListingAcmeTeam reservationListingAcmeTeam);
}
