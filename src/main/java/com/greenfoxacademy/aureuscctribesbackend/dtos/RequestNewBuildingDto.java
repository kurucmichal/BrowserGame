package com.greenfoxacademy.aureuscctribesbackend.dtos;

import javax.validation.constraints.NotNull;

public class RequestNewBuildingDto {

  @NotNull(message = "type is not specified")
  private String type;

  public RequestNewBuildingDto() {
  }

  public RequestNewBuildingDto(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
