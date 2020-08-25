package com.acme.reservation.email;

import com.acme.reservation.entity.Email;

public interface EmailSender {

  void sendEmail(Email to, String text);
}
