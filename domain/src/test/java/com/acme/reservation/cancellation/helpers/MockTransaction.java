package com.acme.reservation.cancellation.helpers;

import org.springframework.transaction.ReactiveTransaction;

public class MockTransaction implements ReactiveTransaction {

  @Override
  public boolean isNewTransaction() {
    return false;
  }

  @Override
  public void setRollbackOnly() {}

  @Override
  public boolean isRollbackOnly() {
    return false;
  }

  @Override
  public boolean isCompleted() {
    return false;
  }
}
