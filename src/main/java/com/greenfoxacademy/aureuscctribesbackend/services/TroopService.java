package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import java.util.NoSuchElementException;
import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import java.util.List;

public interface TroopService {

  Troop saveTroop(Troop troop);

  Troop getTroopById(Long id) throws NoSuchElementException;

  Troop create(Kingdom kingdom);

  Troop upgrade(Troop troop, int newLevel, Kingdom kingdom);

  int attackUpgrade(int newLevel);

  int defenseUpgrade(int newLevel);

  int hpUpgrade(int newLevel);

  TroopDto convertTroopToDto(Troop troop);

  List<TroopDto> listAllTroopsDto(Kingdom kingdom);

  Troop convertDtoToTroop(TroopDto dto);

  List<Troop> listTroopsByLevel(int level, Kingdom kingdom);

  double calculateMultiplier(int baseValue, int currentLevel, int newLevel);
}
