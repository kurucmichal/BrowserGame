package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.NoBuildingFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ResourceInvalidException;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import org.hibernate.procedure.NoSuchParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService {

  private final BuildingServiceImpl buildingService;
  private final ResourceServiceImpl resourceService;
  private final TroopServiceImpl troopService;

  @Autowired
  public PurchaseServiceImpl(BuildingServiceImpl buildingService,
      ResourceServiceImpl resourceService, TroopServiceImpl troopService) {
    this.buildingService = buildingService;
    this.resourceService = resourceService;
    this.troopService = troopService;
  }


  @Override
  public Building purchaseBuilding(Kingdom kingdom, String buildingType)
      throws ResourceInvalidException, NoSuchParameterException {

    String type = buildingType.toLowerCase();

    try {
      if (isAffordable(kingdom, 1, type)) {
        int price = getPrice(1, type);
        resourceService.updateResource(kingdom.getId(), price, 0);
        return buildingService.create(kingdom, type);

      }
      throw new ResourceInvalidException("Not enough resources to build: " + buildingType);

    } catch (NoSuchParameterException e) {
      throw new NoSuchParameterException("Unknown object type: " + buildingType);
    }

  }

  @Override
  public boolean postBuilding(Kingdom kingdom, Building building)
      throws ResourceInvalidException, NoSuchParameterException {

    int level = building.getLevel();
    String type = building.getClass().getSimpleName().toLowerCase();

    if (isAffordable(kingdom, level, type)) {
      int price = getPrice(level, type);
      resourceService.updateResource(kingdom.getId(), price, 0);
      return true;

    } else {
      throw new ResourceInvalidException("Not enough resources to build: " + type);

    }
  }

  @Override
  public Building upgradeBuilding(Long buildingId)
      throws NoBuildingFoundException, ResourceInvalidException {

    Building building = buildingService.getById(buildingId);
    int currentLevel = building.getLevel();
    int newLevel = currentLevel + 1;
    Kingdom kingdom = building.getKingdom();
    String buildingType = building.getClass().getSimpleName().toLowerCase();

    if (isAffordable(kingdom, newLevel, buildingType)) {
      int price = getPrice(newLevel, buildingType);
      resourceService.updateResource(kingdom.getId(), price, 0);
      return buildingService.upgrade(building, newLevel);

    }
    throw new ResourceInvalidException("Not enough resources to get a building: " + buildingType);
  }

  @Override
  public Troop purchaseTroop(Kingdom kingdom)
      throws ResourceInvalidException, NoSuchParameterException {

    if (isAffordable(kingdom, 1, "troop")) {
      int price = getPrice(1, "troop");
      resourceService.updateResource(kingdom.getId(), price, 0);
      return troopService.create(kingdom);

    }
    throw new ResourceInvalidException("Not enough resources to get a troop");
  }

  @Override
  public boolean postTroop(Kingdom kingdom, int level) throws ResourceInvalidException {

    if (isAffordable(kingdom, level, "troop")) {
      int price = getPrice(level, "troop");
      resourceService.updateResource(kingdom.getId(), price, 0);
      return true;

    } else {
      throw new ResourceInvalidException("Not enough resources to get a troop");
    }
  }

  @Override
  public Troop upgradeTroop(Troop troop) throws ResourceInvalidException {

    int currentLevel = troop.getLevel();
    int newLevel = currentLevel + 1;
    Kingdom kingdom = troop.getKingdom();

    if (isAffordable(kingdom, newLevel, "troop")) {
      int price = getPrice(newLevel, "troop");
      resourceService.updateResource(kingdom.getId(), price, 0);
      return troopService.upgrade(troop, newLevel, kingdom);
    }
    throw new ResourceInvalidException(
        "Not enough resources (gold = " + kingdom.getGold().getQuantity()
            + ") to get a troop upgrade to level: " + newLevel);
  }


  @Override
  public boolean isAffordable(Kingdom kingdom, int level, String objectType)
      throws NoSuchParameterException {
    int goldAmount = resourceService.getGold(kingdom).getQuantity();

    switch (objectType) {
      case "troop":
        return goldAmount >= level * 25;

      case "farm":
      case "mine":
        return goldAmount >= level * 100;

      case "academy":
        if (level == 1) {
          return goldAmount >= 150;

        } else {
          return goldAmount >= level * 100;

        }
      case "townhall":
        return goldAmount >= level * 200;

      default:
        throw new NoSuchParameterException("Unknown object type: " + objectType);
    }
  }

  @Override
  public int getPrice(int level, String objectType) {
    switch (objectType) {
      case "troop":
        return level * 25;

      case "farm":
      case "mine":
        return level * 100;

      case "academy":
        if (level == 1) {
          return 150;

        } else {
          return level * 100;

        }
      case "townhall":
        return level * 200;

      default:
        throw new NoSuchParameterException("Unknown object type: " + objectType);
    }
  }
}
