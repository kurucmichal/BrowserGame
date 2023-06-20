package com.greenfoxacademy.aureuscctribesbackend.models;

import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Troop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String type;
  @NotNull
  private Integer level;

  @NotNull
  private Integer hp;

  @NotNull
  private Integer attack;

  @NotNull
  private Integer defense;


  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime startedAt;


  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime finishedAt;


  @ManyToOne(fetch = FetchType.EAGER)
  private Kingdom kingdom;

  private boolean valid;


  public Troop() {
  }

  public Troop(Kingdom kingdom) {
    this.level = 1;
    this.hp = 20;
    this.attack = 10;
    this.defense = 5;
    this.startedAt = LocalDateTime.now();
    this.finishedAt = LocalDateTime.now().plusSeconds(30L);
    this.kingdom = kingdom;
    this.valid = isValid();
  }

  public Troop(String type, Integer level, Integer hp, Integer attack, Integer defense,
      LocalDateTime startedAt, LocalDateTime finishedAt) {
    this.type = type;
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defense = defense;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
    this.valid = isValid();
  }

  public boolean isValid() {
    return level != null
        && hp != null
        && attack != null
        && defense != null
        && startedAt != null
        && finishedAt != null
        && kingdom != null
        && startedAt.isBefore(finishedAt);
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

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public Integer getHp() {
    return hp;
  }

  public void setHp(Integer hp) {
    this.hp = hp;
  }

  public Integer getAttack() {
    return attack;
  }

  public void setAttack(Integer attack) {
    this.attack = attack;
  }

  public Integer getDefense() {
    return defense;
  }

  public void setDefense(Integer defense) {
    this.defense = defense;
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

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }
}
