package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.services.BuildingServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.PurchaseServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.ResourceServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.TroopServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TroopController.class)
@RunWith(SpringRunner.class)
public class TroopControllerPutTest {


  @MockBean
  private KingdomServiceImpl kingdomService;

  @MockBean
  private ResourceServiceImpl resourceService;

  @MockBean
  private PurchaseServiceImpl purchaseService;

  @MockBean
  private BuildingServiceImpl buildingService;

  @MockBean
  private PlayerServiceImpl playerService;

  @MockBean
  private TroopServiceImpl troopService;

  @MockBean
  private LoggingServiceImpl loggingService;
  @Mock
  private Authentication authentication;

  @MockBean
  private HttpServletRequest request;

  @Autowired
  private MockMvc mockMvc;


  @WithMockUser(value = "TestUser")
  @Test
  public void upgradeTroopWithLevel1Successfully() throws Exception {

    String username = "testUser";
    Player player = new Player();
    player.setId(1L);
    player.setPlayerName(username);
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setPlayer(player);
    Troop troop1 = new Troop();
    troop1.setId(1L);
    troop1.setLevel(1);
    troop1.setType("Infantry");
    Troop troopUpgraded = new Troop();
    troop1.setId(1L);
    troop1.setLevel(2);
    troop1.setType("Infantry");
    Troop troop2 = new Troop();
    troop2.setId(2L);
    troop2.setLevel(2);
    troop2.setType("Infantry");
    Troop troop3 = new Troop();
    troop3.setId(3L);
    troop3.setLevel(3);
    troop3.setType("Infantry");
    kingdom.getTroops().add(troop1);
    kingdom.getTroops().add(troop2);
    kingdom.getTroops().add(troop3);
    kingdom.getTroops().add(troopUpgraded);

    List<Troop> listLevels = new ArrayList<>();
    listLevels.add(troop1);
    TroopDto troopDto1 = new TroopDto();
    troopDto1.setLevel(2);
    TroopDto troopDto2 = new TroopDto();
    troopDto2.setLevel(2);
    TroopDto troopDto3 = new TroopDto();
    troopDto3.setLevel(3);
    List<TroopDto> expectedTroopDtos = new ArrayList<>();
    expectedTroopDtos.add(troopDto1);
    expectedTroopDtos.add(troopDto2);
    expectedTroopDtos.add(troopDto3);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(username);
    when(playerService.getPlayerByPlayerName(anyString())).thenReturn(player);
    when(kingdomService.getKingdomByPlayer(any(Player.class))).thenReturn(kingdom);
    when(troopService.listTroopsByLevel(anyInt(), any(Kingdom.class))).thenReturn(listLevels);
    when(purchaseService.upgradeTroop(any(Troop.class))).thenReturn(troopUpgraded);
    when(troopService.listAllTroopsDto(any(Kingdom.class))).thenReturn(expectedTroopDtos);

    mockMvc.perform(put("/kingdom/troop/1").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].level", is(2)))
        .andExpect(jsonPath("$[1].level", is(2)))
        .andExpect(jsonPath("$[2].level", is(3)))
        .andDo(print());
  }

  @WithMockUser(value = "TestUser")
  @Test
  public void upgradeTroopWithLevel1NotSuccessfully() throws Exception {

    String username = "testUser";
    Player player = new Player();
    player.setId(1L);
    player.setPlayerName(username);
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setPlayer(player);
    Troop troop1 = new Troop();
    troop1.setId(1L);
    troop1.setLevel(1);
    troop1.setType("Infantry");
    Troop troopUpgraded = new Troop();
    troop1.setId(1L);
    troop1.setLevel(2);
    troop1.setType("Infantry");
    Troop troop2 = new Troop();
    troop2.setId(2L);
    troop2.setLevel(2);
    troop2.setType("Infantry");
    Troop troop3 = new Troop();
    troop3.setId(3L);
    troop3.setLevel(3);
    troop3.setType("Infantry");
    kingdom.getTroops().add(troop1);
    kingdom.getTroops().add(troop2);
    kingdom.getTroops().add(troop3);
    kingdom.getTroops().add(troopUpgraded);

    List<Troop> listLevels = new ArrayList<>();
    listLevels.add(troop1);
    TroopDto troopDto1 = new TroopDto();
    troopDto1.setLevel(2);
    TroopDto troopDto2 = new TroopDto();
    troopDto2.setLevel(2);
    TroopDto troopDto3 = new TroopDto();
    troopDto3.setLevel(3);
    List<TroopDto> expectedTroopDtos = new ArrayList<>();
    expectedTroopDtos.add(troopDto1);
    expectedTroopDtos.add(troopDto2);
    expectedTroopDtos.add(troopDto3);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(username);
    when(playerService.getPlayerByPlayerName(anyString())).thenReturn(player);
    when(kingdomService.getKingdomByPlayer(any(Player.class))).thenReturn(kingdom);
    when(troopService.listTroopsByLevel(anyInt(), any(Kingdom.class))).thenThrow(
        NoSuchElementException.class);

    mockMvc.perform(put("/kingdom/troop/1").contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("400 - Bad Request")))
        .andExpect(jsonPath("$.path", is("/kingdom/troop/1")))
        .andDo(print());
  }
}