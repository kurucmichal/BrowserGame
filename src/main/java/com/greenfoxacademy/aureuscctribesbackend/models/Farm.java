package com.greenfoxacademy.aureuscctribesbackend.models;

import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "farm")
public class Farm extends Building {

  public Farm() {
    this.setFinishedAt(LocalDateTime.now().plusSeconds(60));
    this.setHp(100);
  }

  public Farm(Kingdom kingdom) {
    super(kingdom);
    this.setFinishedAt(LocalDateTime.now().plusSeconds(60));
    this.setHp(100);
  }
}
