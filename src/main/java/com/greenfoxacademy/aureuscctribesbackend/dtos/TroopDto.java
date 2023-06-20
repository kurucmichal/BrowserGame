package com.greenfoxacademy.aureuscctribesbackend.dtos;

import java.time.LocalDateTime;

public class TroopDto {

  private int level;
  private int hp;
  private int attack;
  private int defense;
  private LocalDateTime startedAt;
  private LocalDateTime finishedAt;
  private String type;


  public TroopDto() {

  }


  public TroopDto(int level, int hp, int attack, int defense, LocalDateTime startedAt,
      LocalDateTime finishedAt, String type) {
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defense = defense;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
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

  public int getAttack() {
    return attack;
  }

  public void setAttack(int attack) {
    this.attack = attack;
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

  public int getDefense() {
    return defense;
  }

  public void setDefense(int defense) {
    this.defense = defense;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}


