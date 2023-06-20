package com.greenfoxacademy.aureuscctribesbackend.dtos;

import static org.junit.Assert.*;

import org.junit.Test;

public class ErrorResponseDtoTest {

  @Test
  public void testGetStatus() {
    ErrorResponseDto errorResponse = new ErrorResponseDto("404", "Not Found");
    assertEquals("404", errorResponse.getStatus());
  }

  @Test
  public void testSetStatus() {
    ErrorResponseDto errorResponse = new ErrorResponseDto("404", "Not Found");
    errorResponse.setStatus("500");
    assertEquals("500", errorResponse.getStatus());
  }

  @Test
  public void testGetMessage() {
    ErrorResponseDto errorResponse = new ErrorResponseDto("404", "Not Found");
    assertEquals("Not Found", errorResponse.getMessage());
  }

  @Test
  public void testSetMessage() {
    ErrorResponseDto errorResponse = new ErrorResponseDto("404", "Not Found");
    errorResponse.setMessage("Internal Server Error");
    assertEquals("Internal Server Error", errorResponse.getMessage());
  }
}