package com.greenfoxacademy.aureuscctribesbackend.dtos;

import com.greenfoxacademy.aureuscctribesbackend.models.Location;
import java.util.List;

public class KingdomDto {

  private Long id;
  private String name;
  private Long userId;

  private List<BuildingDto> buildings;
  private List<ResourcesDto> resources;
  private List<TroopDto> troops;
  private Location location;

  public KingdomDto() {

  }

  public KingdomDto(Long id, String name, Long userId, List<BuildingDto> buildings,
      List<ResourcesDto> resources, List<TroopDto> troops, Location location) {
    this.id = id;
    this.name = name;
    this.userId = userId;
    this.buildings = buildings;
    this.resources = resources;
    this.troops = troops;
    this.location = location;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }


  public List<ResourcesDto> getResources() {
    return resources;
  }

  public void setResources(List<ResourcesDto> resources) {
    this.resources = resources;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public List<TroopDto> getTroops() {
    return troops;
  }

  public void setTroops(List<TroopDto> troops) {
    this.troops = troops;
  }

  public List<BuildingDto> getBuildings() {
    return buildings;
  }

  public void setBuildings(
      List<BuildingDto> buildings) {
    this.buildings = buildings;
  }
}
