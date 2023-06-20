package com.greenfoxacademy.aureuscctribesbackend.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class LoggingServiceImpl implements LoggingService {


  private Environment env;

  @Autowired
  public LoggingServiceImpl(Environment env) {
    this.env = env;
  }

  @Override
  public void logInfo(int statusCode, String message, HttpServletRequest request) {
    String logLevel = env.getProperty("TRIBES_LOG_LVL");
    if ("INFO".equals(logLevel) || "DEBUG".equals(logLevel)) {
      String formattedResponse = getFormattedInfo(statusCode, message, request);
      System.out.println(formattedResponse);
    }
  }

  @Override
  public void logError(int statusCode, String message, HttpServletRequest request) {
    String logLevel = env.getProperty("TRIBES_LOG_LVL");
    if ("ERROR".equals(logLevel) || "INFO".equals(logLevel) || "DEBUG".equals(logLevel)) {
      String formattedError = getFormattedError(statusCode, message, request);
      System.err.println(formattedError);
    }
  }

  @Override
  public String getFormattedInfo(int statusCode, String message, HttpServletRequest request) {
    String requestDetails = String.format("%s %s : ", request.getMethod(), request.getRequestURI());
    String timestamp = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    return String.format("%s  INFO  %d  --- %s%s", timestamp, statusCode, requestDetails, message);
  }

  @Override
  public String getFormattedError(int statusCode, String message, HttpServletRequest request) {
    String requestDetails = String.format("%s %s : ", request.getMethod(), request.getRequestURI());
    String timestamp = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    return String.format("%s  ERROR %d  --- %s%s", timestamp, statusCode, requestDetails, message);
  }
}
