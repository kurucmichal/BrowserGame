package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class KingdomServiceIntegrationTest {

  @Autowired
  private PlayerService playerService;


  @Autowired
  private KingdomServiceImpl kingdomService;

  @Test
  public void testCreateNewKingdom() {
    Player player = new Player();
    player.setPlayerName("John Doe");
    player.setPassword("password");
    player.setEmail("test@test.com");
    player = playerService.savePlayer(player);

    Kingdom kingdom = kingdomService.createNewKingdom(player);

    assertNotNull(kingdom.getId());
    assertEquals("John Doe", kingdom.getName());
    assertEquals(player.getId(), kingdom.getPlayer().getId());

  }

  @Test
  public void testGetByPlayerIdOptional() {
    Player player = new Player();
    player.setPlayerName("John Doe");
    player.setPassword("password");
    player.setEmail("test@test.com");
    player = playerService.savePlayer(player);

    Kingdom kingdom = kingdomService.createNewKingdom(player);

    Kingdom optionalKingdom = kingdomService.getByPlayerIdOptional(player.getId());

    assertEquals(kingdom.getId(), optionalKingdom.getId());
  }

  @Test
  public void testListAllKingdomDto() {
    Player player = new Player();
    player.setPlayerName("John Doe");
    player.setPassword("password");
    player.setEmail("test@test.com");
    player = playerService.savePlayer(player);

    Kingdom kingdom = kingdomService.createNewKingdom(player);

    List<KingdomDto> kingdomDtos = kingdomService.listAllKingdomDto();

    assertEquals(1, kingdomDtos.size());

    KingdomDto kingdomDto = kingdomDtos.get(0);
    assertEquals(kingdom.getId(), kingdomDto.getId());
    assertEquals(kingdom.getName(), kingdomDto.getName());
    assertNotNull(kingdomDto.getResources());
    assertNotNull(kingdomDto.getTroops());
  }
}


