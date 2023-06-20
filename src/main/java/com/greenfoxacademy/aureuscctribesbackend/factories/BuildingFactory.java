package com.greenfoxacademy.aureuscctribesbackend.factories;

import com.greenfoxacademy.aureuscctribesbackend.models.Academy;
import com.greenfoxacademy.aureuscctribesbackend.models.Farm;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Mine;
import com.greenfoxacademy.aureuscctribesbackend.models.TownHall;

public class BuildingFactory {

  public Farm createFarm(Kingdom kingdom) {
    return new Farm(kingdom);
  }

  public Mine createMine(Kingdom kingdom) {
    return new Mine(kingdom);
  }

  public TownHall createTownHall(Kingdom kingdom) {
    return new TownHall(kingdom);
  }

  public Academy createAcademy(Kingdom kingdom) {
    return new Academy(kingdom);
  }
}
