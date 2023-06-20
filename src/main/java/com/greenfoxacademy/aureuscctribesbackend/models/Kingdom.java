package com.greenfoxacademy.aureuscctribesbackend.models;

import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Kingdom {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String name;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private Food food;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private Gold gold;

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  private List<Troop> troops = new ArrayList<>();

  @Embedded
  private Location location;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "player_id", nullable = false)
  private Player player;

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
  private List<Building> buildings = new ArrayList<>();

  @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
  private List<Farm> farms = new ArrayList<>();

  public Kingdom() {
  }

  public Kingdom(String name) {
    this.name = name;
    this.troops = new ArrayList<>();
    this.location = new Location();
    this.farms = new ArrayList<>();
    this.buildings = new ArrayList<>();
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

  public Food getFood() {
    return food;
  }

  public void setFood(Food food) {
    this.food = food;
  }

  public Gold getGold() {
    return gold;
  }

  public void setGold(Gold gold) {
    this.gold = gold;
  }

  public List<Troop> getTroops() {
    return troops;
  }

  public void setTroops(List<Troop> troops) {
    this.troops = troops;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public List<Building> getBuildings() {
    return buildings;
  }

  public void setBuildings(
      List<Building> buildings) {
    this.buildings = buildings;
  }

  public List<Farm> getFarms() {
    return farms;
  }

  public void setFarms(List<Farm> farms) {
    this.farms = farms;
  }
}
