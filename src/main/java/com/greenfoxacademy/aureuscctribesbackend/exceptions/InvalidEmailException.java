package com.greenfoxacademy.aureuscctribesbackend.exceptions;

public class InvalidEmailException extends RuntimeException {

  public InvalidEmailException(String message) {
    super(message);
  }
}
