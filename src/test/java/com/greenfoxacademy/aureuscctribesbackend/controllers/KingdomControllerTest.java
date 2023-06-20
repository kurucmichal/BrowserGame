package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerIdNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Location;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.FoodRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.GoldRepository;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(KingdomController.class)
@RunWith(SpringRunner.class)
public class KingdomControllerTest {

  @MockBean
  private KingdomService kingdomService;

  @MockBean
  private PlayerService playerService;

  @MockBean
  private PlayerRepository playerRepository;

  @MockBean
  private KingdomRepository kingdomRepository;

  @MockBean
  private GoldRepository goldRepository;

  @MockBean
  private FoodRepository foodRepository;

  @MockBean
  private LoggingService loggingService;

  @Autowired
  private MockMvc mockMvc;

  @WithMockUser(value = "TestUser")
  @Test
  public void testGetKingdomsWithNoKingdoms() throws Exception {
    Mockito.when(kingdomService.listAllKingdomDto()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/api/kingdoms"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @WithMockUser(value = "TestUser")
  @Test
  public void testGetKingdomsWithKingdoms() throws Exception {
    Player player1 = new Player();
    player1.setId(1L);
    KingdomDto kingdomDto1 = new KingdomDto();
    kingdomDto1.setId(1L);
    kingdomDto1.setName("Kingdom 1");
    kingdomDto1.setLocation(new Location(15, 30));
    kingdomDto1.setUserId(player1.getId());

    Player player2 = new Player();
    player2.setId(2L);
    KingdomDto kingdomDto2 = new KingdomDto();
    kingdomDto2.setId(2L);
    kingdomDto2.setName("Kingdom 2");
    kingdomDto2.setLocation(new Location(30, 50));
    kingdomDto2.setUserId(player2.getId());

    List<KingdomDto> mockedKingdomDtosList = Arrays.asList(kingdomDto1, kingdomDto2);
    Mockito.when(kingdomService.listAllKingdomDto()).thenReturn(mockedKingdomDtosList);

    mockMvc.perform(get("/api/kingdoms"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].userId", is(1)))
        .andExpect(jsonPath("$[0].name", is("Kingdom 1")))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].userId", is(2)))
        .andExpect(jsonPath("$[1].name", is("Kingdom 2")))
        .andDo(print());
  }

  @WithMockUser(value = "TestUser")
  @Test
  public void getKingdomById() throws Exception {
    long playerId = 1L;
    Player player = new Player();
    player.setId(playerId);
    player.setPlayerName("TestPlayer");
    Kingdom kingdom = new Kingdom();
    kingdom.setName("TestKingdom");
    kingdom.setPlayer(player);
    KingdomDto kingdomDto = new KingdomDto();
    kingdomDto.setName(kingdom.getName());
    kingdomDto.setUserId(playerId);
    when(playerService.getPlayerById(playerId)).thenReturn(player);
    when(kingdomService.getKingdomByPlayerId(playerId)).thenReturn(kingdom);
    when(kingdomService.convertKingdomToDto(kingdom)).thenReturn(kingdomDto);

    mockMvc.perform(get("/api/kingdom/{id}", playerId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(kingdom.getName())))
        .andDo(print());
  }

  @WithMockUser(value = "TestUser")
  @Test
  public void getKingdomByIdWhenPlayerNotFound() throws Exception {
    long playerId = 1L;
    when(playerService.getPlayerById(playerId)).thenThrow(
        new PlayerIdNotFoundException("Player not found with ID: " + playerId));

    mockMvc.perform(get("/api/kingdom/{id}", playerId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", is("Player not found with ID: " + playerId)));
  }

  @WithMockUser(value = "TestUser")
  @Test
  public void getKingdomByIdWhenKingdomNotFound() throws Exception {
    long playerId = 1L;
    Player player = new Player();
    player.setId(playerId);
    player.setPlayerName("TestPlayer");
    when(playerService.getPlayerById(playerId)).thenReturn(player);
    when(kingdomService.getKingdomByPlayerId(playerId)).thenThrow(
        new ObjectNotFoundException(
            "Kingdom not found for player with name: " + player.getPlayerName()
                + ", and with ID: "
                + playerId));

    mockMvc.perform(get("/api/kingdom/{id}", playerId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", is("Kingdom not found for player with name: " + player.getPlayerName()
            + ", and with ID: "
            + playerId)));
  }
}