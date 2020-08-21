package com.acme.reservation.persistence;

import com.acme.reservation.persistence.model.CustomerPersistence;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CustomerCrudRepository extends ReactiveCrudRepository<CustomerPersistence, Long> {}
