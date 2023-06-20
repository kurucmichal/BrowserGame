package com.greenfoxacademy.aureuscctribesbackend.services.schedulers;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import com.greenfoxacademy.aureuscctribesbackend.services.BuildingService;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.services.ResourceService;
import com.greenfoxacademy.aureuscctribesbackend.services.TroopService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class FoodConsumptionTest {

  @Autowired
  private KingdomServiceImpl kingdomService;
  @Autowired
  private BuildingService buildingService;
  @Autowired
  private ResourceService resourceService;
  @Autowired
  private TroopService troopService;
  @Autowired
  private PlayerService playerService;

  @Autowired
  private FoodConsumption foodConsumption;

  @Test
  public void subtractFood() {
    Player player = new Player("marek");
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    player.setPassword(passwordEncoder.encode("1234"));
    player.setRoleType(RoleType.USER);
    player.setVerified(true);
    player.setEmail("test@test.cz");
    playerService.savePlayer(player);
    Kingdom kingdom = kingdomService.createNewKingdom(player);
    kingdom.setPlayer(player);
    kingdomService.saveKingdom(kingdom);

    List<Troop> troopList = new ArrayList<>();
    Troop troop = new Troop(kingdom);
    troop.setFinishedAt(LocalDateTime.now());
    troopService.saveTroop(troop);
    troopList.add(troop);
    kingdomService.saveKingdom(kingdom);

    foodConsumption.subtractFood();
    Kingdom kingdom1 = kingdomService.getKingdomByPlayer(player);

    assertEquals(45, resourceService.getFood(kingdom1).getQuantity());
  }
}