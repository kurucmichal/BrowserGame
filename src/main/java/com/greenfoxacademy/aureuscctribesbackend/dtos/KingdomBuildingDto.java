package com.greenfoxacademy.aureuscctribesbackend.dtos;

public class KingdomBuildingDto {
  private String kingdomName;
  private int buildingCount;

  public KingdomBuildingDto(String kingdomName, int buildingCount) {
    this.kingdomName = kingdomName;
    this.buildingCount = buildingCount;
  }

  public String getKingdomName() {
    return kingdomName;
  }

  public int getBuildingCount() {
    return buildingCount;
  }
}
