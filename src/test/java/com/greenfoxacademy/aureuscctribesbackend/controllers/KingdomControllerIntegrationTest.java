package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.FoodRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.GoldRepository;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.testutils.PlayerITModel;
import java.util.List;
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
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Transactional
public class KingdomControllerIntegrationTest {

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private KingdomService kingdomService;

  @Autowired
  private LoggingService loggingService;

  @Autowired
  private PlayerService playerService;

  @Autowired
  private FoodRepository foodRepository;

  @Autowired
  private GoldRepository goldRepository;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(applicationContext).apply(springSecurity())
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
  public void getKingdomsTest() throws Exception {
    Player player = new Player("TestUser");
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    player.setPassword(passwordEncoder.encode("password"));
    player.setId(1L);
    player.setEmail("test@test.com");
    player.setRoleType(RoleType.USER);
    playerService.addPlayer(player);

    String kingdomName = "Kingdom 1";
    Kingdom kingdom = kingdomService.createKingdom(kingdomName, player);
    kingdomService.saveKingdom(kingdom);

    KingdomDto kingdomDto = kingdomService.convertKingdomToDto(kingdom);
    List<KingdomDto> expectedKingdomDtos = kingdomService.listAllKingdomDto();

    mockMvc.perform(get("/api/kingdoms").with(user(setupPlayer())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("Kingdom 1")))
        .andExpect(jsonPath("$[0].id", is(kingdomDto.getId().intValue())))
        .andExpect(jsonPath("$[0].userId", is(player.getId().intValue())))
        .andExpect(jsonPath("$", hasSize(1)))
        .andDo(print());
  }

  @Test
  public void getKingdomsEmptyTest() throws Exception {
    List<KingdomDto> expectedKingdomDtos = kingdomService.listAllKingdomDto();

    mockMvc.perform(get("/api/kingdoms").with(user(setupPlayer())))
        .andExpect(status().isOk())
        .andExpect(content().string("[]"))
        .andDo(print());
  }
}
