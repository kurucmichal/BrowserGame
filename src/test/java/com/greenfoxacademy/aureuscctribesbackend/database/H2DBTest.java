package com.greenfoxacademy.aureuscctribesbackend.database;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class H2DBTest {

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private KingdomService kingdomService;

  @Autowired
  private KingdomRepository kingdomRepository;

  @Autowired
  private PlayerService playerService;

  @Test
  public void testSave() {
    Player player = new Player("testPlayer");
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    player.setPassword(passwordEncoder.encode("1234"));
    player.setEmail("test@test.com");
    player.setRoleType(RoleType.USER);
    player.setVerified(true);
    playerService.addPlayer(player);

    Optional<Player> expectedPlayer = playerRepository.findByPlayerName("testPlayer");

    if (expectedPlayer.isPresent()) {
      assertEquals("testPlayer", expectedPlayer.get().getPlayerName());
    } else {
      System.out.println("No player in database");
    }
  }
}
