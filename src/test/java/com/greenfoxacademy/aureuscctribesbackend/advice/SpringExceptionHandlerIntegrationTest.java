package com.greenfoxacademy.aureuscctribesbackend.advice;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SpringExceptionHandlerIntegrationTest {

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
    testUser.setEmail("test@gmail.com");
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

  @Test
  public void testNoHandlerExceptionIsThrown() throws Exception {

    mockMvc.perform(get("/random").with(user(initiateSecurityContext())))
        .andExpect(jsonPath("$.status", is("404 - Not Found")))
        .andExpect(jsonPath("$.message", is("No handler found for GET /random")))
        .andExpect(jsonPath("$.path", is("/random")));
  }

  @Test
  public void noAuthenticationProvided() throws Exception {

    mockMvc.perform(get("/kingdom/buildings"))
        .andExpect(jsonPath("$.status", is("401 - Unauthorized")))
        .andExpect(
            jsonPath("$.message", is("Full authentication is required to access this resource")));
  }
}