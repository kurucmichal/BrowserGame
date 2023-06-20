package com.greenfoxacademy.aureuscctribesbackend.models;

import java.time.LocalDateTime;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "academy")
public class Academy extends Building {

  public Academy() {
    this.setFinishedAt(LocalDateTime.now().plusSeconds(90));
    this.setHp(150);
  }

  public Academy(Kingdom kingdom) {
    super(kingdom);
    this.setFinishedAt(LocalDateTime.now().plusSeconds(90));
    this.setHp(150);
  }
}
