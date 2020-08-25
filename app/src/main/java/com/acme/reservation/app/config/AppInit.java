package com.acme.reservation.app.config;

import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.application.usecases.creation.CreateReservationUseCase;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Email;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class AppInit {

  public static final int TOTAL_RESERVATIONS_TO_CREATE = 100;

  private final CreateReservationUseCase createReservationUseCase;

  @Autowired
  public AppInit(CreateReservationUseCase createReservationUseCase) {
    this.createReservationUseCase = createReservationUseCase;
  }

  @PostConstruct
  private void init() {
    getRandomReservations().flatMap(createReservationUseCase::createReservation).subscribe();
  }

  private Flux<CreateReservationDto> getRandomReservations() {
    Stream<CreateReservationDto> createReservationDtoStream =
        IntStream.range(0, TOTAL_RESERVATIONS_TO_CREATE).mapToObj(r -> createReservationDto());
    return Flux.fromStream(createReservationDtoStream);
  }

  private CreateReservationDto createReservationDto() {
    return CreateReservationDto.builder()
        .price(new Money(BigDecimal.valueOf(100)))
        .cancellationPolicy(
            RandomUtils.nextBoolean() ? CancellationPolicy.FLEX : CancellationPolicy.STRICT)
        .customer(getRandomCustomer())
        .startDate(LocalDateTime.now())
        .endDate(LocalDateTime.now().plusDays(5))
        .destination(getRandomDestination())
        .build();
  }

  private Customer getRandomCustomer() {
    return Customer.builder()
        .email(new Email(getRandomString() + "@acme.com"))
        .firstName(getRandomString())
        .build();
  }

  private String getRandomString() {
    return RandomStringUtils.randomAlphabetic(5);
  }

  private Destination getRandomDestination() {
    return Destination.builder().timeZone(ZoneId.systemDefault()).name(getRandomString()).build();
  }
}
