package com.greenfoxacademy.aureuscctribesbackend.exceptions;

import org.springframework.security.core.AuthenticationException;

public class UsernameAlreadyUsedException extends AuthenticationException {

  public UsernameAlreadyUsedException(String msg) {
    super(msg);
  }
}
