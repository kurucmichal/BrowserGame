package com.greenfoxacademy.aureuscctribesbackend.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.aureuscctribesbackend.models.ErrorResponse;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

  private final LoggingService loggingService;

  public RestAccessDeniedHandler(LoggingService loggingService) {
    this.loggingService = loggingService;
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    loggingService.logInfo(HttpStatus.UNAUTHORIZED.value(),
        "Handling the authorization exception in Access denied handler", request);

    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    mapper.writeValue(response.getOutputStream(), new ErrorResponse(HttpStatus.FORBIDDEN,
        accessDeniedException.getMessage(), request.getRequestURI()));
    loggingService.logError(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage(), request);

  }
}

