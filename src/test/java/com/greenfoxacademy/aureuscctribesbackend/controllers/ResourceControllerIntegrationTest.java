package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.ResourceService;
import com.greenfoxacademy.aureuscctribesbackend.testutils.PlayerITModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ResourceControllerIntegrationTest {

  @Autowired
  private KingdomServiceImpl kingdomService;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private KingdomRepository kingdomRepository;

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private ResourceService resourceService;

  @Autowired
  private LoggingService loggingService;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(applicationContext)
        .apply(springSecurity())
        .build();
  }

  private PlayerITModel setupPlayer() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Player testUser = new Player("TestUser");
    testUser.setId(1L);
    testUser.setPassword(passwordEncoder.encode("password"));
    testUser.setEmail("test@test.com");
    testUser.setRoleType(RoleType.USER);

    return new PlayerITModel(testUser);
  }

  @Test
  public void getResourcesByKingdomTest() throws Exception {
    Player player = new Player("TestUser");
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    player.setPassword(passwordEncoder.encode("password"));
    player.setEmail("test@test.com");
    player.setRoleType(RoleType.USER);
    playerRepository.save(player);
    Kingdom kingdom = kingdomService.createNewKingdom(player);
    kingdomRepository.save(kingdom);

    mockMvc.perform(get("/api/kingdom/resources").with(user(setupPlayer())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].quantity", is(50)))
        .andExpect(jsonPath("$[1].quantity", is(50)))
        .andDo(print());
  }
}
