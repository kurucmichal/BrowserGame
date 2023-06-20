package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import com.greenfoxacademy.aureuscctribesbackend.dtos.ResourcesDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.services.ResourceService;
import java.util.Arrays;
import java.util.List;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ResourceController.class)
@RunWith(SpringRunner.class)
public class ResourceControllerTest {

  @MockBean
  private KingdomService kingdomService;

  @MockBean
  private PlayerService playerService;

  @MockBean
  private ResourceService resourceService;

  @MockBean
  private Authentication authentication;

  @MockBean
  private LoggingService loggingService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getResourcesFromKingdomTest() throws Exception {
    Player player = new Player();
    player.setId(1L);
    player.setPlayerName("testPlayer");
    when(authentication.getName()).thenReturn(player.getPlayerName());
    when(playerService.getPlayerByPlayerName("testPlayer")).thenReturn(player);

    Kingdom kingdom = new Kingdom();
    kingdom.setName("testKingdom");
    kingdom.setId(1L);
    kingdom.setPlayer(player);

    Food food = new Food();
    food.setQuantity(100);

    Gold gold = new Gold();
    gold.setQuantity(500);

    kingdom.setFood(food);
    kingdom.setGold(gold);

    when(kingdomService.getKingdomByPlayer(player)).thenReturn(kingdom);

    ResourcesDto resourcesDto1 = new ResourcesDto("food", food.getQuantity());
    ResourcesDto resourcesDto2 = new ResourcesDto("gold", gold.getQuantity());
    List<ResourcesDto> mockedResources = Arrays.asList(resourcesDto1, resourcesDto2);
    Mockito.when(resourceService.getResourcesDtoFromKingdom(kingdom)).thenReturn(mockedResources);

    mockMvc.perform(get("/api/kingdom/resources")
            .with(user("testPlayer")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].resourceType", is("food")))
        .andExpect(jsonPath("$[0].quantity", is(100)))
        .andExpect(jsonPath("$[1].resourceType", is("gold")))
        .andExpect(jsonPath("$[1].quantity", is(500)))
        .andDo(print());
  }

  @Test
  public void getResourcesFromKingdomWhenKingdomNotFoundTest() throws Exception {
    Player player = new Player();
    player.setId(1L);
    player.setPlayerName("testPlayer");
    when(authentication.getName()).thenReturn(player.getPlayerName());
    when(playerService.getPlayerByPlayerName("testPlayer")).thenReturn(player);

    when(kingdomService.getKingdomByPlayer(player)).thenThrow(new ObjectNotFoundException(
        "Kingdom not found for player with name: " + player.getPlayerName()));

    mockMvc.perform(get("/api/kingdom/resources")
            .with(user("testPlayer")))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", is("Kingdom not found for player with name: " + player.getPlayerName())));
  }
}