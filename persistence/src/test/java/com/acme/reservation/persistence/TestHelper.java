package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.LoadReservationDto;
import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Email;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.entity.ReservationStatus;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import com.acme.reservation.persistence.model.CustomerPersistence;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import reactor.core.publisher.Mono;

public class TestHelper {

  public static final BigDecimal PRICE = BigDecimal.valueOf(100);
  private final ReservationRepository reservationRepository;

  public static final String EMAIL = "david@acme.com";
  public static final String FIRST_NAME = "David";
  public static final String LAST_NAME = "Gomez";
  public static final String DESTINATION_NAME = getRandomString();

  private static String getRandomString() {
    return RandomStringUtils.randomAlphabetic(5);
  }

  public static final Long ID = RandomUtils.nextLong();

  public TestHelper(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  public Mono<Reservation> createReservation(Reservation reservation) {

    Mono<ReservationId> reservationIdMono =
        this.reservationRepository.createReservation(reservation);

    return reservationIdMono.flatMap(this.reservationRepository::getReservationById);
  }

  public Mono<Reservation> createReservationAndCancelledFullRefund() {
    return createReservationAndCancelledFullRefund(createReservation());
  }

  public Mono<Reservation> createReservationAndCancelledFullRefund(Reservation reservation) {
    return createReservationAndCancelled(
        reservation, reservation.getTotalRefundableMoneyToCustomer());
  }

  public Mono<Reservation> createReservationAndCancelled(Reservation reservation, Money refund) {
    Mono<Reservation> reservationMono = createReservation(reservation);
    Mono<Reservation> reservationFullyRefunded =
        reservationMono.map(
            r -> {
              r.cancel(new RefundBreakdown(refund));
              return r;
            });

    return reservationFullyRefunded
        .flatMap(r -> reservationRepository.updateStatus(r).thenReturn(r.getReservationId()))
        .flatMap(reservationRepository::getReservationById);
  }

  public static Customer createCustomer() {
    return Customer.builder()
        .email(new Email(EMAIL))
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .build();
  }

  public static Destination createDestination() {
    return Destination.builder().timeZone(ZoneId.systemDefault()).name(DESTINATION_NAME).build();
  }

  public static CustomerPersistence createCustomerPersistence() {
    return CustomerPersistence.builder()
        .email(EMAIL)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .id(ID)
        .build();
  }

  public static Reservation createReservation() {
    LoadReservationDto dto =
        LoadReservationDto.builder()
            .cancellationPolicy(CancellationPolicy.FLEX)
            .customer(createCustomer())
            .destination(createDestination())
            .reservationId(new ReservationId(getRandomString()))
            .status(ReservationStatus.CREATED)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(5))
            .price(new Money(PRICE))
            .creationTimestamp(Instant.now())
            .build();
    return new Reservation(dto);
  }

  public static Reservation createCompletedReservation() {
    LoadReservationDto dto =
        LoadReservationDto.builder()
            .cancellationPolicy(CancellationPolicy.FLEX)
            .customer(createCustomer())
            .destination(createDestination())
            .reservationId(new ReservationId(getRandomString()))
            .status(ReservationStatus.COMPLETED)
            .startDate(LocalDateTime.now().minusDays(30))
            .endDate(LocalDateTime.now().minusDays(25))
            .price(new Money(PRICE))
            .creationTimestamp(Instant.now())
            .build();
    return new Reservation(dto);
  }
}
