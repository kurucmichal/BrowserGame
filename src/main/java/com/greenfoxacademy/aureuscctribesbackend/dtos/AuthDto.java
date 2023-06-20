package com.greenfoxacademy.aureuscctribesbackend.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthDto {

  private String token;

  public AuthDto() {
  }

  public AuthDto(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}