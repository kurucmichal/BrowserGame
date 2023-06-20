package com.greenfoxacademy.aureuscctribesbackend.dtos;

import java.time.LocalDateTime;

public class BuildingDto {

  private Long id;
  private String type;
  private int level;
  private int hp;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;

  public BuildingDto() {

  }

  public BuildingDto(Long id, String type, int level, int hp, LocalDateTime startedAt,
      LocalDateTime finishedAt) {
    this.id = id;
    this.type = type;
    this.level = level;
    this.hp = hp;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public LocalDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(LocalDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public LocalDateTime getFinishedAt() {
    return finishedAt;
  }

  public void setFinishedAt(LocalDateTime finishedAt) {
    this.finishedAt = finishedAt;
  }
}

