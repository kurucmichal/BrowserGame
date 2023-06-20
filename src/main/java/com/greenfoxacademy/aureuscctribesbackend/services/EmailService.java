package com.greenfoxacademy.aureuscctribesbackend.services;

public interface EmailService {

  void sendEmail(String receiverEmail, String emailTemplate);
}
