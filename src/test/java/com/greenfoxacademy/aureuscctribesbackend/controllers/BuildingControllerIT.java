package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Farm;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Mine;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import com.greenfoxacademy.aureuscctribesbackend.repositories.BuildingRepository;
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
import org.springframework.http.MediaType;
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
public class BuildingControllerIT {

  @Autowired
  private KingdomServiceImpl kingdomService;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private KingdomRepository kingdomRepository;

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private BuildingRepository buildingRepository;

  @Autowired
  private ResourceService resourceService;

  @Autowired
  private LoggingService loggingService;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {

    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(applicationContext).apply(springSecurity())
        .build();
  }

  private PlayerITModel setupPlayer() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Player testUser = new Player("zbyna");
    testUser.setId(1L);
    testUser.setPassword(passwordEncoder.encode("1234"));
    testUser.setEmail("test@test.com");
    testUser.setRoleType(RoleType.USER);

    return new PlayerITModel(testUser);
  }

  @Test
  public void getKingdomBuildingsTest() throws Exception {
    Player player = new Player("zbyna");
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    player.setPassword(passwordEncoder.encode("1234"));
    player.setEmail("test@test.com");
    player.setRoleType(RoleType.USER);
    playerRepository.save(player);
    Kingdom kingdom = kingdomService.createNewKingdom(player);
    kingdomRepository.save(kingdom);

    mockMvc.perform(get("/kingdom/buildings").with(user(setupPlayer())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].type", is("townhall")))
        .andExpect(jsonPath("$[0].level", is(1)))
        .andExpect(jsonPath("$[0].hp", is(200)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].type", is("farm")))
        .andExpect(jsonPath("$[1].level", is(1)))
        .andExpect(jsonPath("$[1].hp", is(100)))
        .andExpect(jsonPath("$[2].id", is(3)))
        .andExpect(jsonPath("$[2].type", is("mine")))
        .andExpect(jsonPath("$[2].level", is(1)))
        .andExpect(jsonPath("$[2].hp", is(100)))
        .andExpect(jsonPath("$[3].id", is(4)))
        .andExpect(jsonPath("$[3].type", is("academy")))
        .andExpect(jsonPath("$[3].level", is(1)))
        .andExpect(jsonPath("$[3].hp", is(150)))
        .andDo(print());
  }

  @Test
  public void testGetKingdomBuildingsByName() throws Exception {
    Player player = new Player("zbyna");
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    player.setPassword(passwordEncoder.encode("1234"));
    player.setEmail("test@test.com");
    player.setRoleType(RoleType.USER);
    playerRepository.save(player);
    Kingdom kingdom = kingdomService.createNewKingdom(player);

    Building building1 = new Farm(kingdom);
    Building building2 = new Farm(kingdom);
    Building building3 = new Mine(kingdom);
    Building building4 = new Mine(kingdom);

    buildingRepository.save(building1);
    buildingRepository.save(building2);
    buildingRepository.save(building3);
    buildingRepository.save(building4);
    kingdomRepository.save(kingdom);

    mockMvc.perform(get("/kingdom/buildings/farm").with(user(setupPlayer())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(2)))
        .andExpect(jsonPath("$[0].type", is("farm")))
        .andExpect(jsonPath("$[0].level", is(1)))
        .andExpect(jsonPath("$[0].hp", is(100)))
        .andExpect(jsonPath("$[1].id", is(5)))
        .andExpect(jsonPath("$[1].type", is("farm")))
        .andExpect(jsonPath("$[1].level", is(1)))
        .andExpect(jsonPath("$[1].hp", is(100)))
        .andExpect(jsonPath("$[2].id", is(6)))
        .andExpect(jsonPath("$[2].type", is("farm")))
        .andExpect(jsonPath("$[2].level", is(1)))
        .andExpect(jsonPath("$[2].hp", is(100)))
        .andDo(print());
  }

  @Test
  public void addNewBuildingTest() throws Exception {
    Player player = new Player("zbyna");
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    player.setPassword(passwordEncoder.encode("1234"));
    player.setEmail("test@test.com");
    player.setRoleType(RoleType.USER);
    playerRepository.save(player);

    Kingdom kingdom = kingdomService.createNewKingdom(player);
    Gold gold = resourceService.getGold(kingdom);
    gold.setQuantity(200);
    resourceService.saveGold(gold);
    kingdomService.saveKingdom(kingdom);

    mockMvc.perform(post("/kingdom/building").with(user(setupPlayer()))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"type\" : \"farm\"}")
            .accept(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.type", is("farm")))
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(100)))
        .andDo(print());
  }

}
