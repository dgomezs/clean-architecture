package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.ReservationId;
import com.acme.reservation.persistence.adapters.CustomerAdapter;
import com.acme.reservation.persistence.adapters.DestinationAdapter;
import com.acme.reservation.persistence.adapters.ReservationAdapter;
import com.acme.reservation.persistence.model.CustomerPersistence;
import com.acme.reservation.persistence.model.DestinationPersistence;
import com.acme.reservation.persistence.model.ReservationPersistence;
import java.util.UUID;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

  private final ReservationCrudRepository reservationCrudRepository;
  private final CustomerCrudRepository customerCrudRepository;
  private final DestinationCrudRepository destinationCrudRepository;
  private final ReservationAdapter reservationAdapter;
  private final CustomerAdapter customerAdapter;
  private final DestinationAdapter destinationAdapter;

  public Mono<ReservationId> createReservation(Reservation reservation) {
    ReservationPersistence reservationPersistence =
        reservationAdapter.toReservationPersistence(reservation);

    reservationPersistence.setReservationId(UUID.randomUUID().toString());
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
    return null;
  }

  public Mono<Void> updateStatus(Reservation reservation) {
    return null;
  }
}
