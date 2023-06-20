package com.greenfoxacademy.aureuscctribesbackend.dtos;

public class BuildingLeaderboardDto {

  private String kingdomName;
  private int buildings;

  public BuildingLeaderboardDto(String kingdomName, int buildings) {
    this.kingdomName = kingdomName;
    this.buildings = buildings;
  }

  public String getKingdomName() {
    return kingdomName;
  }

  public void setKingdomName(String kingdomName) {
    this.kingdomName = kingdomName;
  }

  public int getBuildings() {
    return buildings;
  }

  public void setBuildings(int buildings) {
    this.buildings = buildings;
  }
}
