package com.acme.reservation.persistence;

import com.acme.reservation.application.response.ReservationListingAcmeTeam;
import com.acme.reservation.application.response.ReservationListingCustomer;
import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Destination;
import com.acme.reservation.entity.Reservation;
import com.acme.reservation.persistence.model.RefundPersistence;
import org.junit.Assert;
import org.springframework.util.StringUtils;

public class VerifyHelper {

  private VerifyHelper() {}

  public static boolean equalReservation(Reservation reservation, Reservation r) {
    return reservation.getStartDate().equals(r.getStartDate())
        && reservation.getEndDate().equals(r.getEndDate())
        && reservation.getStatus().equals(r.getStatus());
  }

  public static boolean equalCustomer(Customer c1, Customer c2) {
    return c1.getFirstName().equals(c2.getFirstName())
        && c1.getLastName().equals(c2.getLastName())
        && c1.getEmail().equals(c2.getEmail());
  }

  public static boolean verifyAcmeTeamListing(
      ReservationListingAcmeTeam r, Reservation reservation) {
    return verifyCustomerListing(r.getReservationListingCustomer(), reservation)
        && equalCustomer(r.getCustomer(), reservation.getCustomer())
        && r.getCustomer().getId() != null;
  }

  public static boolean verifyCustomerListing(
      ReservationListingCustomer r, Reservation reservation) {
    return r.getStartDate().equals(reservation.getStartDate())
        && !StringUtils.isEmpty(r.getReservationId())
        && equalDestination(r.getDestination(), reservation.getDestination())
        && r.getDestination().getId() != null;
  }

  public static boolean equalDestination(Destination d1, Destination d2) {
    return d1.getName().equals(d2.getName())
        && d1.getTimeZone().getId().equals(d2.getTimeZone().getId());
  }

  public static void verifyIsAFullRefund(RefundPersistence r) {
    Assert.assertEquals(r.getRefundAmount().doubleValue(), TestHelper.PRICE.doubleValue(), 0.0001);
  }
}
