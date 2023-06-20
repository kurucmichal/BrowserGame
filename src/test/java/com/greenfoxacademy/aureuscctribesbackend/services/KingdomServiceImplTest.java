package com.greenfoxacademy.aureuscctribesbackend.services;


import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Academy;
import com.greenfoxacademy.aureuscctribesbackend.models.Farm;
import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerIdNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Location;
import com.greenfoxacademy.aureuscctribesbackend.models.Mine;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.TownHall;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.ResourceRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KingdomServiceImplTest {

  @InjectMocks
  private KingdomServiceImpl kingdomService;

  @Mock
  private KingdomRepository kingdomRepository;
  @Mock
  private ResourceRepository resourceRepository;

  @Mock
  PlayerServiceImpl playerService;

  @Mock
  private ResourceService resourceService;


  private Player player;
  private Kingdom kingdom;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    player = new Player("John");
    kingdom = new Kingdom();
  }

  @Mock
  private PlayerRepository playerRepository;

  @Mock
  private TroopService troopService;

  @Mock
  private BuildingService buildingService;


  @Test
  public void saveKingdom() {
    Kingdom kingdom = new Kingdom();
    doReturn(kingdom).when(kingdomRepository).save(any(Kingdom.class));
    kingdomService.saveKingdom(kingdom);
    verify(kingdomRepository).save(kingdom);
  }

  @Test
  public void testFindByLocation() {
    Location location = new Location(10, 20);
    Kingdom kingdom = new Kingdom("Test Kingdom");
    kingdom.setLocation(location);
    doReturn(Optional.of(kingdom)).when(kingdomRepository).findByLocation(location);
    Kingdom result = kingdomService.findByLocation(location);
    assertEquals(kingdom, result);
  }

  @Test
  public void testGenerateRandomLocation() {
    int maxX = 1000;
    int maxY = 1000;
    int minX = 0;
    int minY = 0;
    int numTries = 100;
    Set<Location> locations = new HashSet<>();
    for (int i = 0; i < numTries; i++) {
      Location location = kingdomService.generateRandomLocation();
      assertTrue(location.getX() >= minX && location.getX() <= maxX);
      assertTrue(location.getY() >= minY && location.getY() <= maxY);
      assertFalse(locations.contains(location));
      locations.add(location);
    }
  }

  @Test
  public void testListAllKingdoms() {
    Player player = new Player("Test Player");
    Kingdom kingdom = new Kingdom();
    kingdom.setName("New Kingdom");
    Location location = new Location(50, 60);
    kingdom.setLocation(location);
    kingdom.setPlayer(player);
    Player player2 = new Player("Test Player 2");
    Kingdom kingdom2 = new Kingdom();
    kingdom2.setName("New Kingdom 2");
    Location location2 = new Location(60, 70);
    kingdom.setLocation(location2);
    kingdom.setPlayer(player2);
    when(kingdomRepository.findAll()).thenReturn(Arrays.asList(kingdom, kingdom2));
    List<Kingdom> result = kingdomService.listAllKingdoms();
    assertThat(result, containsInAnyOrder(kingdom, kingdom2));
  }

  @Test
  public void testCreateNewKingdom() {

    Kingdom kingdom = new Kingdom();
    kingdom.setName(player.getPlayerName());
    kingdom.setPlayer(player);
    Location location = new Location(100, 200);
    kingdom.setLocation(location);

    Food food = new Food();
    food.setQuantity(50);
    when(resourceService.saveFood(any(Food.class))).thenReturn(food);
    food.setKingdom(kingdom);
    kingdom.setFood(food);

    Gold gold = new Gold();
    gold.setQuantity(50);
    when(resourceService.saveGold(any(Gold.class))).thenReturn(gold);
    gold.setKingdom(kingdom);
    kingdom.setGold(gold);

    TownHall townHall = new TownHall(kingdom);
    Farm farm = new Farm(kingdom);
    Mine mine = new Mine(kingdom);
    Academy academy = new Academy(kingdom);

    when(kingdomRepository.save(any(Kingdom.class))).thenReturn(kingdom);

    Kingdom result = kingdomService.createNewKingdom(player);

    assertNotNull(result);
    assertEquals(player.getPlayerName(), result.getName());
    assertNotNull(result.getGold());
    assertNotNull(result.getFood());
    assertNotNull(result.getBuildings().add(new TownHall()));
    assertNotNull(result.getBuildings().add(new Farm()));
    assertNotNull(result.getBuildings().add(new Mine()));
    assertNotNull(result.getBuildings().add(new Academy()));


  }

  @Test
  public void getKingdomByPlayerIdWhenPlayerExists() {
    long playerId = 1L;
    Player player = new Player();
    player.setId(playerId);
    player.setPlayerName("Test");
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setName("TestKingdom");
    kingdom.setPlayer(player);

    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(kingdomRepository.findByPlayer(player)).thenReturn(Optional.of(kingdom));

    Kingdom result = kingdomService.getKingdomByPlayerId(playerId);

    assertEquals(kingdom, result);
  }

  @Test(expected = PlayerIdNotFoundException.class)
  public void getKingdomByPlayerIdWhenPlayerDoesNotExist() {
    long playerId = 1L;
    when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

    kingdomService.getKingdomByPlayerId(playerId);
  }

  @Test(expected = ObjectNotFoundException.class)
  public void getKingdomByPlayerIdWhenKingdomNotFound() {
    long playerId = 1L;
    Player player = new Player();
    player.setId(playerId);
    player.setPlayerName("Test");

    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
    when(kingdomRepository.findByPlayer(player)).thenReturn(Optional.empty());

    kingdomService.getKingdomByPlayerId(playerId);
  }

  @Test
  public void testGetByPlayerIdOptionalWithExistingId() {
    Long playerId = 1L;
    Kingdom kingdom = new Kingdom();
    when(kingdomRepository.findByPlayerId(playerId)).thenReturn(Optional.of(kingdom));

    Kingdom result = kingdomService.getByPlayerIdOptional(playerId);

    assertEquals(kingdom, result);
  }

  @Test(expected = ObjectNotFoundException.class)
  public void testGetByPlayerIdOptionalWithNonExistingId() {
    Long playerId = 1L;
    when(kingdomRepository.findByPlayerId(playerId)).thenReturn(Optional.empty());

    kingdomService.getByPlayerIdOptional(playerId);
  }

  @Test
  public void testConvertKingdomToDto() {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setName("Test Kingdom");

    Player player = new Player();
    player.setId(2L);
    kingdom.setPlayer(player);

    Location location = new Location();
    kingdom.setLocation(location);

    KingdomDto dto = kingdomService.convertKingdomToDto(kingdom);

    assertNotNull(dto);
    assertEquals(kingdom.getId(), dto.getId());
    assertEquals(kingdom.getName(), dto.getName());
    assertEquals(player.getId(), dto.getUserId());
    assertEquals(location, dto.getLocation());
  }
}