package com.greenfoxacademy.aureuscctribesbackend.exceptions;

import org.springframework.http.converter.HttpMessageNotReadableException;

public class RequestBodyMissingException extends HttpMessageNotReadableException {

  public RequestBodyMissingException(String message) {
    super(message);
  }
}
