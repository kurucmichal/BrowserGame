package com.greenfoxacademy.aureuscctribesbackend.advice;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.InvalidEmailException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.MissingCriteriaException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.NoBuildingFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerIdNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerNameNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.RequestBodyMissingException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ResourceInvalidException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.UnknownErrorException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.UsernameAlreadyUsedException;
import com.greenfoxacademy.aureuscctribesbackend.models.ErrorResponse;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.procedure.NoSuchParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Environment environment;
  private final LoggingService loggingService;


  @Autowired
  public GlobalExceptionHandler(Environment environment, LoggingService loggingService) {
    this.environment = environment;
    this.loggingService = loggingService;
  }

  //500 internal server error
  @ExceptionHandler({UnknownErrorException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleUnknownErrorException(Exception exception,
      HttpServletRequest request) {

    String path = request.getRequestURI();
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        environment.getProperty("config.errors.unknown_error"),
        path
    );
    loggingService.logError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(),
        request);
    return errorResponse;
  }

  //400 bad request
  @ExceptionHandler({InvalidEmailException.class,
      RequestBodyMissingException.class,
      MissingCriteriaException.class,
      ResourceInvalidException.class,
      NoSuchParameterException.class,
      NoSuchElementException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleInvalidEmailException(Exception exception,
      HttpServletRequest request) {

    String path = request.getRequestURI();
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST,
        exception.getMessage(),
        path
    );
    loggingService.logError(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request);
    return errorResponse;
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleInvalidBodyException(MethodArgumentNotValidException exception,
      HttpServletRequest request) {

    String path = request.getRequestURI();
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST,
        exception.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(
            Collectors.toList()).toString(),
        path
    );
    loggingService.logError(HttpStatus.BAD_REQUEST.value(),  exception.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(
        Collectors.toList()).toString(), request);
    return errorResponse;
  }

  @ExceptionHandler({HttpMessageNotReadableException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleBodyException(HttpMessageNotReadableException exception,
      HttpServletRequest request) throws IOException {

    String path = request.getRequestURI();
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST,
        exception.getMessage(),
        path
    );
    loggingService.logError(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request);
    return errorResponse;
  }


  //401 unauthorized
  @ExceptionHandler({SignatureException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleNotAuthorizedException(HttpServletRequest request,
      Exception exception) {

    String path = request.getRequestURI();
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.UNAUTHORIZED,
        exception.getMessage(),
        path
    );
    loggingService.logError(HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), request);
    return errorResponse;
  }

  //404 not found
  @ExceptionHandler({NoBuildingFoundException.class,
      PlayerIdNotFoundException.class,
      PlayerNameNotFoundException.class,
      ObjectNotFoundException.class,
      HttpRequestMethodNotSupportedException.class,
      MethodArgumentTypeMismatchException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNotFoundException(HttpServletRequest request, Exception exception) {

    String path = request.getRequestURI();
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND,
        exception.getMessage(),
        path
    );
    loggingService.logError(HttpStatus.NOT_FOUND.value(), exception.getMessage(), request);
    return errorResponse;
  }

  //  409 conflict
  @ExceptionHandler({UsernameAlreadyUsedException.class})
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleConflictException(HttpServletRequest request,
      Exception exception) {

    String path = request.getRequestURI();
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.CONFLICT,
        exception.getMessage(),
        path
    );
    loggingService.logError(HttpStatus.CONFLICT.value(), exception.getMessage(), request);
    return errorResponse;
  }

}
