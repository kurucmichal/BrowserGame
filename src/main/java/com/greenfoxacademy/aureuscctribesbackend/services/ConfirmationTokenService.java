package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.models.ConfirmationToken;


public interface ConfirmationTokenService {

  ConfirmationToken saveConfirmationToken(ConfirmationToken token);

  ConfirmationToken getConfirmationToken(String token);

  void deleteConfirmationToken(ConfirmationToken confirmationToken);
}
