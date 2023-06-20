package com.greenfoxacademy.aureuscctribesbackend.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.aureuscctribesbackend.models.ErrorResponse;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final LoggingService loggingService;

  public RestAuthenticationEntryPoint(LoggingService loggingService) {
    this.loggingService = loggingService;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    loggingService.logInfo(HttpStatus.UNAUTHORIZED.value(),
        "Handling the authentication exception in Authentication entry point", request);

    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.writeValue(response.getOutputStream(), new ErrorResponse(HttpStatus.UNAUTHORIZED,
        authException.getMessage(), request.getRequestURI()));
    loggingService.logError(HttpStatus.UNAUTHORIZED.value(), authException.getMessage(), request);

  }
}
