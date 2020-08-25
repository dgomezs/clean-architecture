package com.acme.reservation.email;

import com.acme.reservation.entity.Email;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailSenderImpl implements EmailSender {

  public void sendEmail(Email to, String text) {
    log.info("Sending email to {} with text {}", to.getEmail(), text);
  }
}
