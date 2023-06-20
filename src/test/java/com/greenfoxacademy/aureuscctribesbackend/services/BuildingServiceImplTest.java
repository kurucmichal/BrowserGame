package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.IncorrectBuildingTypeException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.NoBuildingFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Academy;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Farm;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Mine;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.TownHall;
import com.greenfoxacademy.aureuscctribesbackend.repositories.BuildingRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BuildingServiceImplTest {

  @Mock
  private BuildingRepository buildingRepository;
  @Mock
  private FarmServiceImpl farmService;
  @Mock
  private MineServiceImpl mineService;
  @Mock
  private TownHallServiceImpl townHallService;
  @Mock
  private AcademyServiceImpl academyService;
  @Mock
  private TimeService timeService;
  @Mock
  private KingdomRepository kingdomRepository;
  @InjectMocks
  private BuildingServiceImpl buildingService;

  private Kingdom kingdom;
  private Building farm;
  private Building mine;
  private Player player;


  @Before
  public void setUp() {
    player = new Player();
    player.setId(1L);
    farm = new Farm();
    mine = new Mine();
    kingdom = new Kingdom();
    kingdom.setName("Test");
    kingdom.setPlayer(player);
    farm.setKingdom(kingdom);
    mine.setKingdom(kingdom);
    this.farm.setKingdom(kingdom);
  }

  @Test
  public void saveBuilding() {
    when(buildingService.save(any(Building.class))).thenReturn(farm);
    assertEquals(farm, buildingService.save(farm));
    verify(buildingRepository).save(farm);
  }

  @Test
  public void createFarmAndReturnFarm() {
    when(farmService.create(kingdom)).thenReturn(farm);
    Building test = buildingService.create(kingdom, "farm");

    assertEquals(1, test.getLevel());
    assertEquals(100, test.getHp());
    assertEquals(kingdom.getName(), test.getKingdom().getName());
  }

  @Test
  public void createMineAndReturnMine() {
    when(mineService.create(kingdom)).thenReturn(mine);
    Building test = buildingService.create(kingdom, "mine");

    assertEquals(1, test.getLevel());
    assertEquals(100, test.getHp());
    assertEquals(kingdom.getName(), test.getKingdom().getName());
  }

  @Test(expected = IncorrectBuildingTypeException.class)
  public void createBuildingWithInvalidBuildingTypeParameter() {
    try {
      buildingService.create(kingdom, "test");

    } catch (IncorrectBuildingTypeException e) {
      verify(buildingRepository, times(0)).save(any(Building.class));
      System.err.println(e.getMessage());
      throw e;
    }
  }

  @Test
  public void testConvertBuildingToDto() {
    Building farm = new Farm();
    farm.setLevel(2);
    farm.setHp(4);
    farm.setStartedAt(LocalDateTime.now());

    BuildingDto dto = buildingService.convertBuildingToDto(farm);

    assertNotNull(dto);
    assertEquals(farm.getLevel(), dto.getLevel());
    assertEquals(farm.getHp(), dto.getHp());
    assertEquals(farm.getStartedAt(), dto.getStartedAt());
  }

  @Test
  public void testListAllBuildingDto() {
    Kingdom kingdom = new Kingdom();

    Building farm = new Farm();
    farm.setLevel(2);
    farm.setHp(4);
    farm.setStartedAt(LocalDateTime.now());

    Building academy = new Academy();
    academy.setLevel(5);
    academy.setHp(6);
    academy.setStartedAt(LocalDateTime.now());

    List<Building> buildings = new ArrayList<>();
    buildings.add(farm);
    buildings.add(academy);

    kingdom.setBuildings(buildings);

    List<BuildingDto> dtos = buildingService.listAllBuildingDto(kingdom);

    assertNotNull(dtos);
    assertEquals(2, dtos.size());

    BuildingDto farmDto = dtos.get(0);
    assertNotNull(farmDto);
    assertEquals(farm.getId(), farmDto.getId());
    assertEquals(farm.getLevel(), farmDto.getLevel());
    assertEquals(farm.getHp(), farmDto.getHp());
    assertEquals(farm.getStartedAt(), farmDto.getStartedAt());

    BuildingDto academyDto = dtos.get(1);
    assertNotNull(academyDto);
    assertEquals(academy.getId(), academyDto.getId());
    assertEquals(academy.getLevel(), academyDto.getLevel());
    assertEquals(academy.getHp(), academyDto.getHp());
    assertEquals(academy.getStartedAt(), academyDto.getStartedAt());
  }

  @Test
  public void getByIdWhenBuildingExists() throws NoBuildingFoundException {
    Long buildingId = 1L;
    Building building = new Farm();
    building.setId(buildingId);
    when(buildingRepository.findById(buildingId)).thenReturn(Optional.of(building));

    Building result = buildingService.getById(buildingId);

    assertEquals(building, result);
    verify(buildingRepository, times(1)).findById(buildingId);
  }

  @Test(expected = NoBuildingFoundException.class)
  public void getByIdWhenBuildingNotExist()
      throws NoBuildingFoundException {
    Long buildingId = 1L;
    when(buildingRepository.findById(buildingId)).thenReturn(Optional.empty());

    buildingService.getById(buildingId);
  }

  @Test
  public void testUpgradeBuilding() {
    Building building = new Farm();
    building.setLevel(1);
    building.setHp(100);
    building.setStartedAt(LocalDateTime.now());
    building.setFinishedAt(LocalDateTime.now().plusMinutes(30));
    building.setId(1L);

    int newLevel = 2;

    when(timeService.calculateBuildingCompletion(newLevel)).thenReturn(
        LocalDateTime.now().plusMinutes(60));
    when(buildingRepository.save(building)).thenReturn(building);

    Building upgradedBuilding = buildingService.upgrade(building, newLevel);

    verify(buildingRepository).save(building);

    assertEquals(newLevel, upgradedBuilding.getLevel());
    assertEquals(building.getClass().getSimpleName().toLowerCase(), upgradedBuilding.getType());
    assertEquals(newLevel * 100, upgradedBuilding.getHp());
    assertEquals(building.getStartedAt(), upgradedBuilding.getStartedAt());
  }

  @Test
  public void testIsBuildingPresentWhenBuildingNotExist() {
    when(kingdomRepository.findByPlayerId(player.getId())).thenReturn(Optional.of(kingdom));

    boolean result = buildingService.isBuildingPresent(player, Mine.class);

    assertFalse(result);
  }

  @Test
  public void testIsBuildingPresentWhenKingdomNotExist() {
    when(kingdomRepository.findByPlayerId(player.getId())).thenReturn(Optional.empty());

    boolean result = buildingService.isBuildingPresent(player, Farm.class);

    assertFalse(result);
  }

  @Test
  public void testIsAcademyBuildingPresent() {
    Building building1 = new Farm();
    Building building2 = new Academy();
    Building building3 = new TownHall();
    kingdom.getBuildings().add(building1);
    kingdom.getBuildings().add(building2);
    kingdom.getBuildings().add(building3);

    boolean result = buildingService.isAcademyBuildingPresent(kingdom);

    assertTrue(result);
  }

  @Test
  public void testIsAcademyBuildingNotPresent() {
    Building building1 = new Farm();
    Building building2 = new Mine();
    Building building3 = new TownHall();
    kingdom.getBuildings().add(building1);
    kingdom.getBuildings().add(building2);
    kingdom.getBuildings().add(building3);

    boolean result = buildingService.isAcademyBuildingPresent(kingdom);

    assertFalse(result);
  }
}