package com.greenfoxacademy.aureuscctribesbackend.dtos;

public class ResourcesDto {

  private String resourceType;
  private int quantity;

  public ResourcesDto(String resourceType, int quantity) {
    this.resourceType = resourceType;
    this.quantity = quantity;
  }

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
