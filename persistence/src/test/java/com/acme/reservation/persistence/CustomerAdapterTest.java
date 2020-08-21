package com.acme.reservation.persistence;

import com.acme.reservation.entity.Customer;
import com.acme.reservation.persistence.adapters.CustomerAdapter;
import com.acme.reservation.persistence.model.CustomerPersistence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class CustomerAdapterTest {

  @Autowired CustomerAdapter customerAdapter;

  @Test
  public void testToCustomerPersistence() {

    Customer customer = TestHelper.createCustomer();
    CustomerPersistence customerPersistence = customerAdapter.toCustomerPersistence(customer);

    Assert.assertEquals(customerPersistence.getEmail(), customer.getEmail().getEmail());
  }

  @Test
  public void testToCustomer() {
    CustomerPersistence customerPersistence = TestHelper.createCustomerPersistence();
    Customer customer = customerAdapter.toCustomer(customerPersistence);

    Assert.assertEquals(customerPersistence.getEmail(), customer.getEmail().getEmail());
  }
}
