package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.persistence.adapters.CustomerAdapter;
import com.acme.reservation.persistence.adapters.DestinationAdapter;
import com.acme.reservation.persistence.adapters.ReservationAdapter;
import com.acme.reservation.persistence.crud.CustomerCrudRepository;
import com.acme.reservation.persistence.crud.DestinationCrudRepository;
import com.acme.reservation.persistence.crud.ReservationCrudRepository;
import com.acme.reservation.persistence.model.CustomerPersistence;
import com.acme.reservation.persistence.model.DestinationPersistence;
import com.acme.reservation.persistence.model.ReservationPersistence;
import com.acme.reservation.persistence.model.ReservationRow;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

  private final ReservationCrudRepository reservationCrudRepository;
  private final CustomerCrudRepository customerCrudRepository;
  private final DestinationCrudRepository destinationCrudRepository;
  private final RefundRepository refundRepository;
  private final ReservationAdapter reservationAdapter;
  private final CustomerAdapter customerAdapter;
  private final DestinationAdapter destinationAdapter;
  private final DatabaseClient databaseClient;

  public Mono<ReservationId> createReservation(Reservation reservation) {

    reservation.setReservationId(new ReservationId(UUID.randomUUID().toString()));

    ReservationPersistence reservationPersistence =
        reservationAdapter.toReservationPersistence(reservation);
    reservationPersistence.setNewReservation(true);

    Mono<CustomerPersistence> customerPersistenceMono = storeCustomer(reservation);
    Mono<DestinationPersistence> destinationPersistenceMono = storeDestination(reservation);

    return customerPersistenceMono
        .zipWith(destinationPersistenceMono)
        .flatMap(
            r -> {
              reservationPersistence.setDestinationId(r.getT2().getId());
              reservationPersistence.setCustomerId(r.getT1().getId());
              return storeReservation(reservationPersistence);
            });
  }

  private Mono<DestinationPersistence> storeDestination(Reservation reservation) {
    DestinationPersistence destinationPersistence =
        destinationAdapter.toDestinationPersistence(reservation.getDestination());
    return destinationCrudRepository.save(destinationPersistence);
  }

  private Mono<CustomerPersistence> storeCustomer(Reservation reservation) {
    CustomerPersistence customerPersistence =
        customerAdapter.toCustomerPersistence(reservation.getCustomer());
    return customerCrudRepository.save(customerPersistence);
  }

  private Mono<ReservationId> storeReservation(ReservationPersistence reservationPersistence) {
    return reservationCrudRepository
        .save(reservationPersistence)
        .map(r -> new ReservationId(r.getReservationId()));
  }

  public Mono<Reservation> getReservationById(ReservationId reservationId) {

    final String sql = SqlQuery.reservationQuery + " WHERE r.reservation_id = $1";

    Mono<ReservationRow> reservationRowMono =
        databaseClient
            .execute(sql)
            .bind("$1", reservationId.getKey())
            .as(ReservationRow.class)
            .fetch()
            .one();
    return reservationRowMono.map(reservationAdapter::toReservation).map(Reservation::new);
  }

  public Mono<Void> updateStatus(Reservation reservation) {
    ReservationPersistence reservationPersistence =
        reservationAdapter.toReservationPersistence(reservation);
    reservationPersistence.setNewReservation(false);

    Mono<Void> refundOperation =
        reservation
            .getCurrentRefund()
            .map(r -> this.refundRepository.storeCurrentRefund(reservation))
            .orElse(Mono.empty());

    return reservationCrudRepository.save(reservationPersistence).and(refundOperation).then();
  }
}
