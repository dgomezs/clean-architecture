package com.acme.reservation.persistence.adapters;

import com.acme.reservation.entity.Customer;
import com.acme.reservation.entity.Email;
import com.acme.reservation.persistence.model.CustomerPersistence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Creates a Spring Bean automatically
public interface CustomerAdapter {
  @Mapping(target = "email", source = "email.email")
  CustomerPersistence toCustomerPersistence(Customer customer);

  Customer toCustomer(CustomerPersistence customerPersistence);

  default Email toEmail(String email) {
    return new Email(email);
  }
}
