package com.greenfoxacademy.aureuscctribesbackend.services;

import javax.servlet.http.HttpServletRequest;

public interface LoggingService {

  void logInfo(int statusCode, String message, HttpServletRequest request);

  void logError(int statusCode, String message, HttpServletRequest request);

  String getFormattedInfo(int statusCode, String message, HttpServletRequest request);

  String getFormattedError(int statusCode, String message, HttpServletRequest request);
}
