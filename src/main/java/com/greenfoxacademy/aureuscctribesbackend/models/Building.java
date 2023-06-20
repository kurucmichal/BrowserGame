package com.greenfoxacademy.aureuscctribesbackend.models;

import com.sun.istack.NotNull;
import java.time.LocalDateTime;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "buildings")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "building_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Building {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private int hp;

  @NotNull
  private int level;

  @NotNull
  private LocalDateTime startedAt;

  @NotNull
  private LocalDateTime finishedAt;

  @ManyToOne
  private Kingdom kingdom;

  //constructors
  public Building() {
    this.level = 1;
    this.startedAt = LocalDateTime.now();
  }

  public Building(Kingdom kingdom) {
    this.level = 1;
    this.kingdom = kingdom;
    this.startedAt = LocalDateTime.now();
  }

  //getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
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

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }

  public String getType() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
  }

  @Override
  public String toString() {
    return "Building{"
        +
        "id=" + id
        +
        ", hp=" + hp
        +
        ", level=" + level
        +
        ", startedAt=" + startedAt
        +
        ", finishedAt=" + finishedAt
        +
        ", kingdom=" + kingdom
        +
        '}';
  }
}
