package com.greenfoxacademy.aureuscctribesbackend.services;


import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.ResourcesDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerIdNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Academy;
import com.greenfoxacademy.aureuscctribesbackend.models.Farm;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Location;
import com.greenfoxacademy.aureuscctribesbackend.models.Mine;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.TownHall;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KingdomServiceImpl implements KingdomService {

  private KingdomRepository kingdomRepository;
  private ResourceService resourceService;
  private PlayerRepository playerRepository;
  private TroopService troopService;
  private BuildingService buildingService;


  @Autowired
  public KingdomServiceImpl(KingdomRepository kingdomRepository, ResourceService resourceService,
      PlayerRepository playerRepository, TroopService troopService,
      BuildingService buildingService) {
    this.kingdomRepository = kingdomRepository;
    this.resourceService = resourceService;
    this.playerRepository = playerRepository;
    this.troopService = troopService;
    this.buildingService = buildingService;

  }

  @Override
  public Kingdom saveKingdom(Kingdom kingdom) {
    return Optional.ofNullable(kingdomRepository.save(kingdom))
        .orElseThrow(() -> new RuntimeException("Failed to save Kingdom"));
    //kingdomRepository.save(kingdom);
  }

  @Override
  public Kingdom findByLocation(Location location) {
    return kingdomRepository.findByLocation(location).get();
  }

  @Override
  public Kingdom createNewKingdom(Player player) {

    Kingdom kingdom = new Kingdom();
    kingdom.setName(player.getPlayerName());
    kingdom.setPlayer(player);
    kingdom.setLocation(generateRandomLocation());

    Food food = new Food();
    food.setQuantity(50);
    food = resourceService.saveFood(food);
    food.setKingdom(kingdom);
    kingdom.setFood(food);

    Gold gold = new Gold();
    gold.setQuantity(50);
    gold = resourceService.saveGold(gold);
    gold.setKingdom(kingdom);
    kingdom.setGold(gold);
    kingdom = saveKingdom(kingdom);

    buildingService.create(kingdom, "townhall");
    buildingService.create(kingdom, "farm");
    buildingService.create(kingdom, "mine");
    buildingService.create(kingdom, "academy");

    return saveKingdom(kingdom);
  }

  @Override
  public List<Kingdom> listAllKingdoms() {
    return kingdomRepository.findAll();
  }

  public Location generateRandomLocation() {
    int maxX = 1000; // Maximum X coordinate
    int maxY = 1000; // Maximum Y coordinate
    int minX = 0; // Minimum X coordinate
    int minY = 0; // Minimum Y coordinate
    int rangeX = maxX - minX + 1;
    int rangeY = maxY - minY + 1;
    Location location = null;
    Optional<Kingdom> existingKingdom;
    do {
      int x = (int) (Math.random() * rangeX) + minX;
      int y = (int) (Math.random() * rangeY) + minY;
      location = new Location(x, y);
      existingKingdom = kingdomRepository.findByLocation(location);
    } while (existingKingdom.isPresent());

    return location;
  }

  @Override
  public Kingdom getKingdomByPlayerId(Long playerId) {
    Optional<Player> player = playerRepository.findById(playerId);

    if (player.isPresent()) {

      Optional<Kingdom> optionalKingdom = kingdomRepository.findByPlayer(player.get());
      if (optionalKingdom.isPresent()) {
        return optionalKingdom.get();

      } else {
        throw new ObjectNotFoundException(
            "Kingdom not found for player with name: " + player.get().getPlayerName()
                + ", and with ID: "
                + playerId);

      }
    } else {
      throw new PlayerIdNotFoundException("Player id not found" + playerId);

    }
  }

  @Override
  public Kingdom getByPlayerIdOptional(Long playerId) {

    Optional<Kingdom> optionalKingdom = kingdomRepository.findByPlayerId(playerId);
    if (!optionalKingdom.isPresent()) {
      throw new ObjectNotFoundException("Kingdom not found for player with id: " + playerId);
    }
    return optionalKingdom.get();
  }

  @Override
  public Kingdom getKingdomByPlayer(Player player) {
    Optional<Kingdom> optionalKingdom = kingdomRepository.findByPlayer(player);

    if (optionalKingdom.isPresent()) {
      return optionalKingdom.get();

    } else {
      throw new ObjectNotFoundException(
          "Kingdom not found for player with name: " + player.getPlayerName());

    }
  }

  @Override
  public KingdomDto convertKingdomToDto(Kingdom kingdom) {

    List<ResourcesDto> resources = resourceService.getResourcesDtoFromKingdom(kingdom);
    List<TroopDto> troops = troopService.listAllTroopsDto(kingdom);
    List<BuildingDto> buildings = buildingService.listAllBuildingDto(kingdom);

    return new KingdomDto(kingdom.getId(), kingdom.getName(), kingdom.getPlayer().getId(),
        buildings, resources, troops, kingdom.getLocation());
  }

  @Override
  public List<KingdomDto> listAllKingdomDto() {
    List<Kingdom> kingdoms = kingdomRepository.findAll();
    List<KingdomDto> kingdomDtos = new ArrayList<>();
    for (Kingdom kingdom : kingdoms) {
      kingdomDtos.add(convertKingdomToDto(kingdom));
    }
    return kingdomDtos;
  }

  @Override
  public Kingdom createKingdom(String kingdomName, Player player) {
    Food food = new Food();
    resourceService.saveFood(food);

    Gold gold = new Gold();
    resourceService.saveGold(gold);

    Kingdom kingdom = new Kingdom();
    kingdom.setName(kingdomName);
    kingdom.setLocation(generateRandomLocation());
    kingdom.setPlayer(player);
    kingdom.setGold(gold);
    kingdom.setFood(food);

    Academy academy = new Academy(kingdom);
    Farm farm = new Farm(kingdom);
    TownHall townHall = new TownHall(kingdom);
    Mine mine = new Mine(kingdom);

    kingdom.getBuildings().add(academy);
    kingdom.getBuildings().add(farm);
    kingdom.getBuildings().add(townHall);
    kingdom.getBuildings().add(mine);

    kingdom = kingdomRepository.save(kingdom);
    buildingService.save(academy);
    buildingService.save(farm);
    buildingService.save(townHall);
    buildingService.save(mine);

    return kingdom;
  }

  @Override
  public Boolean kingdomExist(String name) {
    return kingdomRepository.existsByName(name);
  }
}
