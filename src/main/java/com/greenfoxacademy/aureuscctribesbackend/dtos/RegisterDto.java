package com.greenfoxacademy.aureuscctribesbackend.dtos;

public class RegisterDto {

  private String username;
  private String password;
  private String email;
  private String kingdomName;


  public RegisterDto() {
  }

  public RegisterDto(String username, String password, String email, String kingdomName) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.kingdomName = kingdomName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getKingdomName() {
    return kingdomName;
  }

  public void setKingdomName(String kingdomName) {
    this.kingdomName = kingdomName;
  }
}