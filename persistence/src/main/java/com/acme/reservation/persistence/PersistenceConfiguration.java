package com.acme.reservation.persistence;

import com.acme.reservation.application.repository.ReservationRepository;
import com.acme.reservation.application.repository.SearchReservationRepository;
import com.acme.reservation.persistence.adapters.CustomerAdapter;
import com.acme.reservation.persistence.adapters.DestinationAdapter;
import com.acme.reservation.persistence.adapters.RefundAdapter;
import com.acme.reservation.persistence.adapters.ReservationAdapter;
import com.acme.reservation.persistence.crud.CustomerCrudRepository;
import com.acme.reservation.persistence.crud.DestinationCrudRepository;
import com.acme.reservation.persistence.crud.RefundCrudRepository;
import com.acme.reservation.persistence.crud.ReservationCrudRepository;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.h2.H2ConnectionOption;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
@ComponentScan(basePackages = {"com.acme.reservation.persistence"})
public class PersistenceConfiguration extends AbstractR2dbcConfiguration {
  @Bean
  public H2ConnectionFactory connectionFactory() {

    return new H2ConnectionFactory(
        H2ConnectionConfiguration.builder()
            .inMemory("testdb")
            .username("sa")
            .property(H2ConnectionOption.DB_CLOSE_DELAY, "-1")
            .build());
  }

  @Autowired
  void initializeDatabase(ConnectionFactory connectionFactory) {
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    Resource[] scripts = new Resource[] {resourceLoader.getResource("classpath:schema.sql")};
    new ResourceDatabasePopulator(scripts).execute(connectionFactory).block();
  }

  @Bean
  RefundRepository refundRepository(
      RefundCrudRepository refundCrudRepository, RefundAdapter refundAdapter) {
    return new RefundRepositoryImpl(refundCrudRepository, refundAdapter);
  }

  @Bean
  public ReservationRepository reservationRepository(
      ReservationCrudRepository reservationCrudRepository,
      CustomerCrudRepository customerCrudRepository,
      DestinationCrudRepository destinationCrudRepository,
      RefundRepository refundRepository,
      ReservationAdapter reservationAdapter,
      CustomerAdapter customerAdapter,
      DestinationAdapter destinationAdapter,
      DatabaseClient databaseClient) {
    return new ReservationRepositoryImpl(
        reservationCrudRepository,
        customerCrudRepository,
        destinationCrudRepository,
        refundRepository,
        reservationAdapter,
        customerAdapter,
        destinationAdapter,
        databaseClient);
  }

  @Bean
  public SearchReservationRepository searchReservationRepository(
      DatabaseClient databaseClient, ReservationAdapter reservationAdapter) {
    return new SearchReservationRepositoryImpl(databaseClient, reservationAdapter);
  }
}
