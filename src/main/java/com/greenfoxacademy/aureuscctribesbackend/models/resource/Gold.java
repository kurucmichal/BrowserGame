package com.greenfoxacademy.aureuscctribesbackend.models.resource;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import javax.persistence.Entity;

@Entity
public class Gold extends Resource {

  public Gold() {
    super();
  }

  public Gold(Kingdom kingdom) {
    super(50, kingdom);
  }

  public Gold(int quantity, Kingdom kingdom) {
    super(quantity, kingdom);
  }

}
