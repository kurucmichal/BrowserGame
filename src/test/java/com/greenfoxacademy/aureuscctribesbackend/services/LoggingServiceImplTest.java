package com.greenfoxacademy.aureuscctribesbackend.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoggingServiceImplTest {

  @Mock
  private Environment env;
  private LoggingService loggingService;


  @Before
  public void setup() {
    MockitoAnnotations.openMocks(this);
    loggingService = new LoggingServiceImpl(env);
  }

  @Test
  public void testLogInfoWithLogLevelInfo() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/kingdoms");
    request.setMethod("GET");
    when(env.getProperty("TRIBES_LOG_LVL")).thenReturn("INFO");
    loggingService.logInfo(200, "No kingdom was found", request);
    verify(env).getProperty("TRIBES_LOG_LVL");
    verifyNoMoreInteractions(env);
  }

  @Test
  public void testLogInfoWithLogLevelDebug() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/kingdoms");
    request.setMethod("GET");
    when(env.getProperty("TRIBES_LOG_LVL")).thenReturn("DEBUG");
    loggingService.logInfo(200, "No kingdom was found", request);
    verify(env).getProperty("TRIBES_LOG_LVL");
    verifyNoMoreInteractions(env);
  }

  @Test
  public void testLogInfoWithLogLevelError() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/kingdoms");
    request.setMethod("GET");
    when(env.getProperty("TRIBES_LOG_LVL")).thenReturn("ERROR");
    loggingService.logInfo(200, "No kingdom was found", request);
    verify(env).getProperty("TRIBES_LOG_LVL");
    verifyNoMoreInteractions(env);
  }

  @Test
  public void testLogErrorWithLogLevelInfo() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/kingdoms");
    request.setMethod("GET");
    when(env.getProperty("TRIBES_LOG_LVL")).thenReturn("INFO");
    loggingService.logError(500, "Internal Server Error", request);
    verify(env).getProperty("TRIBES_LOG_LVL");
    verifyNoMoreInteractions(env);
  }

  @Test
  public void testLogErrorWithLogLevelDebug() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/kingdoms");
    request.setMethod("GET");
    when(env.getProperty("TRIBES_LOG_LVL")).thenReturn("DEBUG");
    loggingService.logError(500, "Internal Server Error", request);
    verify(env).getProperty("TRIBES_LOG_LVL");
    verifyNoMoreInteractions(env);
  }

  @Test
  public void testLogErrorWithLogLevelError() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/kingdoms");
    request.setMethod("GET");
    when(env.getProperty("TRIBES_LOG_LVL")).thenReturn("ERROR");
    loggingService.logError(500, "Internal Server Error", request);
    verify(env).getProperty("TRIBES_LOG_LVL");
    verifyNoMoreInteractions(env);
  }
}

