package com.greenfoxacademy.aureuscctribesbackend.services;

import java.time.LocalDateTime;
import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.repositories.TroopRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TroopServiceImpl implements TroopService {

  private final TroopRepository troopRepository;
  private final TimeService timeService;

  @Autowired
  public TroopServiceImpl(TroopRepository troopRepository, TimeService timeService) {
    this.troopRepository = troopRepository;
    this.timeService = timeService;
  }

  @Override
  public Troop saveTroop(Troop troop) {
    return troopRepository.save(troop);
  }

  @Override
  public Troop getTroopById(Long id) throws NoSuchElementException {
    Optional<Troop> optionalTroop = troopRepository.findById(id);
    if (optionalTroop.isPresent()) {
      return optionalTroop.get();
    } else {
      throw new NoSuchElementException("Troop not found with id: " + id);
    }
  }

  @Override
  public Troop create(Kingdom kingdom) {
    Troop troop = new Troop(kingdom);
    return saveTroop(troop);
  }

  @Override
  public Troop upgrade(Troop troop, int newLevel, Kingdom kingdom) {
    int currentLevel = troop.getLevel();
    if (newLevel <= currentLevel) {
      throw new IllegalArgumentException("New level should be greater than current level");
    }

    double attackMultiplier = calculateMultiplier(troop.getAttack(), currentLevel, newLevel);
    double defenseMultiplier = calculateMultiplier(troop.getDefense(), currentLevel, newLevel);

    troop.setLevel(newLevel);
    troop.setHp(hpUpgrade(newLevel));
    troop.setAttack((int) Math.round(troop.getAttack() * attackMultiplier));
    troop.setDefense((int) Math.round(troop.getDefense() * defenseMultiplier));;
    troop.setStartedAt(LocalDateTime.now());
    troop.setFinishedAt(timeService.calculateTroopCompletion(newLevel));
    return troopRepository.save(troop);
  }

  @Override
  public int attackUpgrade(int newLevel) {
    return newLevel * 10;
  }

  @Override
  public int defenseUpgrade(int newLevel) {
    return newLevel * 5;
  }

  @Override
  public int hpUpgrade(int newLevel) {
    return newLevel * 20;
  }

  @Override
  public TroopDto convertTroopToDto(Troop troop) {
    return new TroopDto(troop.getLevel(), troop.getHp(), troop.getAttack(), troop.getDefense(),
        troop.getStartedAt(), troop.getFinishedAt(), troop.getType());
  }

  @Override
  public List<TroopDto> listAllTroopsDto(Kingdom kingdom) {
    List<Troop> troops = kingdom.getTroops();
    List<TroopDto> troopsDtos = new ArrayList<>();
    for (Troop troop : troops) {
      troopsDtos.add(convertTroopToDto(troop));
    }
    return troopsDtos;
  }

  @Override
  public Troop convertDtoToTroop(TroopDto dto) {
    Troop troop = new Troop();
    troop.setLevel(dto.getLevel());
    troop.setHp(dto.getHp());
    troop.setAttack(dto.getAttack());
    troop.setDefense(dto.getDefense());
    troop.setStartedAt(dto.getStartedAt());
    troop.setFinishedAt(dto.getFinishedAt());
    troop.setType(dto.getType());
    return troop;
  }

  @Override
  public List<Troop> listTroopsByLevel(int level, Kingdom kingdom) {
    List<Troop> troops = kingdom.getTroops();
    List<Troop> levelTroops = troops.stream()
        .filter(troop -> troop.getLevel() == level)
        .collect(Collectors.toList());

    if (levelTroops.isEmpty()) {
      throw new NoSuchElementException("Kingdom has no troop with level: " + level);
    }
    return levelTroops;
  }

  @Override
  public double calculateMultiplier(int baseValue, int currentLevel, int newLevel) {
    double baseMultiplier = 5.0;
    if (currentLevel > 1) {
      baseMultiplier /= currentLevel;
    }
    return baseMultiplier * newLevel / baseValue;
  }
}
