package com.greenfoxacademy.aureuscctribesbackend.security;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;

public class CustomExceptionFilter extends ExceptionTranslationFilter {

  public CustomExceptionFilter(
      AuthenticationEntryPoint authenticationEntryPoint) {
    super(authenticationEntryPoint);
  }
}
