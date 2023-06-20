package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.IncorrectBuildingTypeException;
import com.greenfoxacademy.aureuscctribesbackend.models.Academy;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.NoBuildingFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.BuildingRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {

  private final BuildingRepository buildingRepository;
  private final FarmServiceImpl farmService;
  private final AcademyServiceImpl academyService;
  private final MineServiceImpl mineService;
  private final TownHallServiceImpl townHallService;
  private final KingdomRepository kingdomRepository;
  private final TimeService timeService;


  @Autowired
  public BuildingServiceImpl(BuildingRepository buildingRepository, FarmServiceImpl farmService,
      AcademyServiceImpl academyService, MineServiceImpl mineService,
      TownHallServiceImpl townHallService, KingdomRepository kingdomRepository,
      TimeService timeService) {

    this.buildingRepository = buildingRepository;
    this.farmService = farmService;
    this.academyService = academyService;
    this.mineService = mineService;
    this.townHallService = townHallService;
    this.kingdomRepository = kingdomRepository;
    this.timeService = timeService;
  }

  @Override
  public Building save(Building building) {
    return buildingRepository.save(building);
  }

  @Override
  public Building create(Kingdom kingdom, String type) throws IncorrectBuildingTypeException {

    switch (type) {
      case "farm":
        return farmService.create(kingdom);

      case "townhall":
        return townHallService.create(kingdom);

      case "academy":
        return academyService.create(kingdom);

      case "mine":
        return mineService.create(kingdom);

      default:
        throw new IncorrectBuildingTypeException(type + " is not supported building type.");
    }
  }

  @Override
  public Building getById(Long id) throws NoBuildingFoundException {
    Optional<Building> optionalBuilding = buildingRepository.findById(id);

    if (!optionalBuilding.isPresent()) {
      throw new NoBuildingFoundException("No building with corresponding id: " + id);
    }
    return optionalBuilding.get();
  }

  @Override
  public Building upgrade(Building building, int newLevel) {

    String buildingType = building.getClass().getSimpleName().toLowerCase();

    building.setLevel(newLevel);
    building.setHp(upgradeHp(newLevel, buildingType));
    building.setStartedAt(LocalDateTime.now());
    building.setFinishedAt(timeService.calculateBuildingCompletion(newLevel));
    return buildingRepository.save(building);

  }

  @Override
  public int upgradeHp(int level, String buildingType) {

    switch (buildingType) {
      case "farm":
      case "mine":
        return (level * 100);

      case "townhall":
        return (level * 200);

      case "academy":
        return (level * 150);

      default:
        throw new NoSuchElementException("No such building type exists " + buildingType);
    }
  }

  @Override
  public BuildingDto convertBuildingToDto(Building building) {
    return new BuildingDto(building.getId(), building.getType(), building.getLevel(),
        building.getHp(), building.getStartedAt(), building.getFinishedAt());
  }

  @Override
  public List<BuildingDto> listAllBuildingDto(Kingdom kingdom) {
    List<Building> buildings = kingdom.getBuildings();
    List<BuildingDto> buildingDtos = new ArrayList<>();
    for (Building building : buildings) {
      buildingDtos.add(convertBuildingToDto(building));
    }
    return buildingDtos;
  }

  @Override
  public boolean isBuildingPresent(Player player, Class<? extends Building> buildingType) {
    Optional<Kingdom> kingdomOptional = kingdomRepository.findByPlayerId(player.getId());
    if (kingdomOptional.isPresent()) {
      Kingdom kingdom = kingdomOptional.get();
      return kingdom.getBuildings().stream()
          .anyMatch(building -> buildingType.isInstance(building));
    }
    return false;
  }

  @Override
  public boolean isAcademyBuildingPresent(Kingdom kingdom) {
    return kingdom.getBuildings().stream()
        .anyMatch(building -> building instanceof Academy);
  }
}
