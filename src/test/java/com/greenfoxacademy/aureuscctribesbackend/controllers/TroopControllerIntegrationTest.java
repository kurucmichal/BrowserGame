package com.greenfoxacademy.aureuscctribesbackend.controllers;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.repositories.BuildingRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.TroopRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.FoodRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.GoldRepository;
import com.greenfoxacademy.aureuscctribesbackend.security.CustomUserDetailsService;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.TroopServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TroopControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  CustomUserDetailsService userDetailsService;
  @Autowired
  PlayerRepository playerRepository;
  @Autowired
  KingdomRepository kingdomRepository;
  @Autowired
  BuildingRepository buildingRepository;
  @Autowired
  TroopRepository troopRepository;
  @Autowired
  FoodRepository foodRepository;
  @Autowired
  GoldRepository goldRepository;
  @Autowired
  KingdomServiceImpl kingdomService;
  @Autowired
  TroopServiceImpl troopService;


  @Before
  public void initiateObjectsForTest() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    Player testUser = new Player("zbyna");
    testUser.setId(1L);
    testUser.setPassword(passwordEncoder.encode("1234"));
    testUser.setRoleType(RoleType.USER);
    testUser.setEmail("zbyna@gmail.com");
    testUser.setVerified(true);
    playerRepository.save(testUser);
    Kingdom kingdom = kingdomService.createNewKingdom(testUser);
    Troop troop1 = new Troop(kingdom);
    troopService.saveTroop(troop1);
    kingdomService.saveKingdom(kingdom);
    playerRepository.save(testUser);
  }

  public UserDetails initiateSecurityContext() {
    return userDetailsService.loadUserByUsername("zbyna");
  }

  @org.junit.Test
  public void upgradeTroopsWithLevel1_returnTroopLevel2() throws Exception {

    mockMvc.perform(put("/kingdom/troop/1").with(user(initiateSecurityContext()))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].level", is(2)))
        .andExpect(jsonPath("$[0].hp", is(40)));
  }

  @org.junit.Test
  public void upgradeTroopsWhenUserNotFound_returnException() throws Exception {

    mockMvc.perform(put("/kingdom/troop/1").with(user("fake"))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("404 - Not Found")))
        .andExpect(jsonPath("$.message", is("Player name not found: fake")))
        .andExpect(jsonPath("$.path", is("/kingdom/troop/1")));
  }

  @WithMockUser("user")
  @Test
  public void upgradeTroopsWhenUserHasNoKingdom_Return404() throws Exception {

    Player player1 = new Player("user");
    PasswordEncoder pswEncoder = new BCryptPasswordEncoder();
    player1.setPassword(pswEncoder.encode("1234"));
    player1.setRoleType(RoleType.USER);
    player1.setVerified(true);
    player1.setEmail("test@gmail.com");
    playerRepository.save(player1);

    mockMvc.perform(put("/kingdom/troop/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.status", is("404 - Not Found")))
        .andExpect(jsonPath("$.message", is("Kingdom not found for player with name: user")))
        .andExpect(jsonPath("$.path", is("/kingdom/troop/1")))
        .andDo(print());
  }
}