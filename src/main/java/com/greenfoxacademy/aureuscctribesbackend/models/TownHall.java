package com.greenfoxacademy.aureuscctribesbackend.models;

import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "townhall")
public class TownHall extends Building {

  public TownHall() {
    this.setFinishedAt(LocalDateTime.now().plusSeconds(120));
    this.setHp(200);
  }

  public TownHall(Kingdom kingdom) {
    super(kingdom);
    this.setFinishedAt(LocalDateTime.now().plusSeconds(120));
    this.setHp(200);
  }

}
