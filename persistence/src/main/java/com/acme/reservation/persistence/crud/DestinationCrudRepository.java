package com.acme.reservation.persistence.crud;

import com.acme.reservation.persistence.model.DestinationPersistence;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationCrudRepository
    extends ReactiveCrudRepository<DestinationPersistence, Long> {}
