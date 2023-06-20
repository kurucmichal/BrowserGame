package com.greenfoxacademy.aureuscctribesbackend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public class ErrorResponse {

  private String status;
  private String message;
  private String path;
  @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timeStamp;

  public ErrorResponse() {
  }

  public ErrorResponse(HttpStatus httpStatus, String message, String path) {
    this.status = getResponseStatus(httpStatus);
    this.message = message;
    this.path = path;
    this.timeStamp = LocalDateTime.now();
  }

  public String getResponseStatus(HttpStatus status) {
    int value = status.value();
    String st = value + " - " + status.getReasonPhrase();
    return st;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public LocalDateTime getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(LocalDateTime timeStamp) {
    this.timeStamp = timeStamp;
  }
}
