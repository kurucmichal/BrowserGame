package com.greenfoxacademy.aureuscctribesbackend.controllers;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.services.BuildingService;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.services.PurchaseServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.TroopService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TroopController.class)
class AnotherFuckingTroopControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private KingdomService kingdomService;
  @MockBean
  private PurchaseServiceImpl purchaseService;

  @MockBean
  private BuildingService buildingService;

  @MockBean
  private PlayerService playerService;

  @MockBean
  private TroopService troopService;

  @MockBean
  private LoggingService loggingService;

  @Mock
  private Authentication authentication;


  @WithMockUser(value = "testUser")
  @Test
  void getKingdomTroops_shouldReturnUnauthorized() throws Exception {
    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);
    TroopDto troopDto = new TroopDto();
    TroopDto troopDto2 = new TroopDto();
    List<TroopDto> troopDtos = Arrays.asList(troopDto, troopDto2);

    when(authentication.getName()).thenReturn("username");
    when(playerService.getPlayerByPlayerName(Mockito.anyString())).thenReturn(player);
    when(kingdomService.getKingdomByPlayerId(anyLong())).thenReturn(kingdom);
    when(troopService.listAllTroopsDto(any(Kingdom.class))).thenReturn(troopDtos);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdom/troops")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .principal(authentication))
        .andExpect(status().isOk());
  }
}
