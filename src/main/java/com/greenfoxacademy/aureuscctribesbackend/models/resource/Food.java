package com.greenfoxacademy.aureuscctribesbackend.models.resource;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import javax.persistence.Entity;

@Entity
public class Food extends Resource {

  public Food() {
    super();
  }

  public Food(Kingdom kingdom) {
    super(50, kingdom);
  }

  public Food(int quantity, Kingdom kingdom) {
    super(quantity, kingdom);
  }

}
