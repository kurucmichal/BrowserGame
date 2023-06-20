package com.greenfoxacademy.aureuscctribesbackend.models;

import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "mine")
public class Mine extends Building {

  public Mine() {
    this.setFinishedAt(LocalDateTime.now().plusSeconds(60));
    this.setHp(100);
  }

  public Mine(Kingdom kingdom) {
    super(kingdom);
    this.setFinishedAt(LocalDateTime.now().plusSeconds(60));
    this.setHp(100);
  }
}
