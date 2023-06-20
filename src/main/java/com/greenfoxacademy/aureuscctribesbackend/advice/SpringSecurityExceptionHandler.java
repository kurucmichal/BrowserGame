package com.greenfoxacademy.aureuscctribesbackend.advice;

import com.greenfoxacademy.aureuscctribesbackend.models.ErrorResponse;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class SpringSecurityExceptionHandler extends ResponseEntityExceptionHandler {

  private final LoggingService loggingService;

  @Autowired
  public SpringSecurityExceptionHandler(LoggingService loggingService) {
    this.loggingService = loggingService;
  }

  //401 missing authorization
  @ExceptionHandler(value = AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  private ErrorResponse handleSecurityException(Exception exception, HttpServletRequest request) {

    ErrorResponse response = new ErrorResponse(
        HttpStatus.FORBIDDEN,
        exception.getMessage(),
        request.getRequestURI()
    );
    loggingService.logError(HttpStatus.FORBIDDEN.value(), exception.getMessage(),
        request);
    return response;
  }

  //403 not authenticated
  @ExceptionHandler({AuthenticationException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  private ErrorResponse handleSecurityExceptiona(Exception exception, HttpServletRequest request) {

    ErrorResponse response = new ErrorResponse(
        HttpStatus.UNAUTHORIZED,
        exception.getMessage(),
        request.getRequestURI()
    );
    loggingService.logError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(),
        request);
    return response;
  }

  //404 not found resource
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    String path = ((ServletWebRequest) request).getRequest().getRequestURI();
    ErrorResponse response = new ErrorResponse(
        HttpStatus.NOT_FOUND,
        ex.getMessage(),
        path
    );
    loggingService.logError(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
        ((ServletWebRequest) request).getRequest());
    return ResponseEntity.status(404).body(response);
  }
}
