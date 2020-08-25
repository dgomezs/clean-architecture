package com.acme.reservation.email;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import com.acme.reservation.application.event.ReservationCancelledEvent;
import com.acme.reservation.application.event.ReservationCreatedEvent;
import com.acme.reservation.application.event.ReservationEvent;
import com.acme.reservation.application.request.CreateReservationDto;
import com.acme.reservation.application.response.RefundBreakdown;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Email;
import com.acme.reservation.entity.Money;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.entity.cancellation.policy.CancellationPolicy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.ReplayProcessor;
import reactor.test.scheduler.VirtualTimeScheduler;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmailTestConfig.class)
public class SendingEmailTest {

  @Autowired private ReservationEmailService reservationEmailService;
  @Autowired private EmailSender emailSender;
  @Autowired private VirtualTimeScheduler virtualTimeScheduler;
  @Autowired ReplayProcessor<ReservationEvent> eventBus;

  @Before
  public void cleanUpMocks() {
    Mockito.reset(emailSender);
    Mockito.doNothing().when(emailSender).sendEmail(any(), anyString());
  }

  @Test
  public void ensureIfAReservationIsCreatedEmailIsSent() {
    Reservation createReservation = createReservation();

    eventBus.onNext(new ReservationCreatedEvent(createReservation));
    Mockito.verify(emailSender, times(1))
        .sendEmail(createReservation.getCustomer().getEmail(), "Reservation created");
  }

  @Test
  public void ensureIfAReservationIsCancelledEmailIsSent() {
    Reservation createReservation = createReservation();
    RefundBreakdown refundBreakdown =
        new RefundBreakdown(createReservation.getTotalRefundableMoneyToCustomer());

    String expectedEmailText = "Reservation cancelled with refund 100.00";

    eventBus.onNext(new ReservationCancelledEvent(createReservation, refundBreakdown));
    Mockito.verify(emailSender, times(1))
        .sendEmail(createReservation.getCustomer().getEmail(), expectedEmailText);
  }

  private Reservation createReservation() {
    CreateReservationDto reservationDto =
        CreateReservationDto.builder()
            .customer(createCustomer())
            .destination(createDestination())
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now())
            .cancellationPolicy(CancellationPolicy.FLEX)
            .price(new Money(BigDecimal.valueOf(100)))
            .build();
    return new Reservation(reservationDto);
  }

  private Destination createDestination() {
    return Destination.builder()
        .name(getRandomString())
        .timeZone(ZoneId.systemDefault())
        .id(RandomUtils.nextLong())
        .build();
  }

  private Customer createCustomer() {
    return Customer.builder()
        .id(RandomUtils.nextLong())
        .email(createEmail())
        .firstName(getRandomString())
        .lastName(getRandomString())
        .build();
  }

  private Email createEmail() {
    return new Email(getRandomString() + "@acme.com");
  }

  private String getRandomString() {
    return RandomStringUtils.randomAlphabetic(5);
  }
}
