package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.assertEquals;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.NoBuildingFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ResourceInvalidException;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.GoldRepository;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import org.hibernate.procedure.NoSuchParameterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PurchaseServiceImplIntegrationTest {


  @Autowired
  PurchaseServiceImpl purchaseService;
  @Autowired
  PlayerRepository playerRepository;
  @Autowired
  KingdomServiceImpl kingdomService;
  @Autowired
  TroopServiceImpl troopService;
  @Autowired
  BuildingServiceImpl buildingService;
  @Autowired
  GoldRepository goldRepository;

  public void initiateKingdomWithResources() {

  }

  @Test
  public void purchaseBuilding_returnBuilding() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Player testUser = new Player("zbyna");
    testUser.setId(1L);
    testUser.setPassword(passwordEncoder.encode("1234"));
    testUser.setRoleType(RoleType.USER);
    testUser.setVerified(true);
    testUser.setEmail("test@gmail.com");
    playerRepository.save(testUser);
    Kingdom kingdom = kingdomService.createNewKingdom(testUser);
    Gold gold = kingdom.getGold();
    gold.setQuantity(1000);
    goldRepository.save(gold);
    Troop troop1 = new Troop(kingdom);
    troopService.saveTroop(troop1);
    kingdomService.saveKingdom(kingdom);
    playerRepository.save(testUser);

    Building returned = purchaseService.purchaseBuilding(kingdom, "farm");

    assertEquals(5, returned.getId().longValue());
    assertEquals(1, returned.getLevel());
    assertEquals("farm", returned.getType());
    assertEquals(kingdom, returned.getKingdom());

  }

  @Test(expected = ResourceInvalidException.class)
  public void purchaseBuildingWithNoResources_ThrowException() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Player testUser = new Player("zbyna");
    testUser.setId(1L);
    testUser.setPassword(passwordEncoder.encode("1234"));
    testUser.setRoleType(RoleType.USER);
    testUser.setVerified(true);
    testUser.setEmail("test@gmail.com");
    playerRepository.save(testUser);
    Kingdom kingdom = kingdomService.createNewKingdom(testUser);
    Troop troop1 = new Troop(kingdom);
    troopService.saveTroop(troop1);
    kingdomService.saveKingdom(kingdom);
    playerRepository.save(testUser);

    Building returned = purchaseService.purchaseBuilding(kingdom, "farm");
  }

  @Test(expected = NoSuchParameterException.class)
  public void purchaseBuildingWithWrongParameter_ThrowException() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Player testUser = new Player("zbyna");
    testUser.setId(1L);
    testUser.setPassword(passwordEncoder.encode("1234"));
    testUser.setRoleType(RoleType.USER);
    testUser.setVerified(true);
    testUser.setEmail("test@gmail.com");
    playerRepository.save(testUser);
    Kingdom kingdom = kingdomService.createNewKingdom(testUser);
    Gold gold = kingdom.getGold();
    gold.setQuantity(1000);
    goldRepository.save(gold);
    Troop troop1 = new Troop(kingdom);
    troopService.saveTroop(troop1);
    kingdomService.saveKingdom(kingdom);
    playerRepository.save(testUser);

    Building returned = purchaseService.purchaseBuilding(kingdom, "fake");
  }

  @Test
  public void upgradeBuilding_returnBuildingWithLevel2() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Player testUser = new Player("zbyna");
    testUser.setId(1L);
    testUser.setPassword(passwordEncoder.encode("1234"));
    testUser.setRoleType(RoleType.USER);
    testUser.setVerified(true);
    testUser.setEmail("test@gmail.com");
    playerRepository.save(testUser);
    Kingdom kingdom = kingdomService.createNewKingdom(testUser);
    Gold gold = kingdom.getGold();
    gold.setQuantity(1000);
    goldRepository.save(gold);
    Troop troop1 = new Troop(kingdom);
    troopService.saveTroop(troop1);
    kingdomService.saveKingdom(kingdom);
    playerRepository.save(testUser);

    Building returned = null;
    try {
      returned = purchaseService.upgradeBuilding(1L);
    } catch (NoBuildingFoundException e) {
      throw new RuntimeException(e);
    }

    assertEquals(1, returned.getId().longValue());
    assertEquals(2, returned.getLevel());
    assertEquals("townhall", returned.getType());
    assertEquals(kingdom.getName(), returned.getKingdom().getName());

  }

  @Test
  public void upgradeTroop_returnTroopWithLevel2() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Player testUser = new Player("zbyna");
    testUser.setId(1L);
    testUser.setPassword(passwordEncoder.encode("1234"));
    testUser.setRoleType(RoleType.USER);
    testUser.setVerified(true);
    testUser.setEmail("test@gmail.com");
    playerRepository.save(testUser);
    Kingdom kingdom = kingdomService.createNewKingdom(testUser);
    Gold gold = kingdom.getGold();
    gold.setQuantity(1000);
    goldRepository.save(gold);
    Troop troop1 = new Troop(kingdom);
    troopService.saveTroop(troop1);
    kingdomService.saveKingdom(kingdom);
    playerRepository.save(testUser);

    Troop returned = null;
    returned = purchaseService.upgradeTroop(troop1);

    assertEquals(1, returned.getId().longValue());
    assertEquals(2, returned.getLevel().intValue());
    assertEquals(40, returned.getHp().intValue());
    assertEquals(kingdom.getName(), returned.getKingdom().getName());
    assertEquals(950, returned.getKingdom().getGold().getQuantity());

  }

}