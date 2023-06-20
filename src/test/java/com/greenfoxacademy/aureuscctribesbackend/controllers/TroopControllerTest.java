package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.services.BuildingServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.PurchaseServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.TroopServiceImpl;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


@RunWith(MockitoJUnitRunner.class)
public class TroopControllerTest {

  @Mock
  private KingdomServiceImpl kingdomService;

  @Mock
  private PurchaseServiceImpl purchaseService;

  @Mock
  private BuildingServiceImpl buildingService;

  @Mock
  private PlayerServiceImpl playerService;

  @Mock
  private TroopServiceImpl troopService;

  @Mock
  private LoggingServiceImpl loggingService;

  @InjectMocks
  private TroopController troopController;

  @Mock
  private Authentication authentication;

  @Mock
  private HttpServletRequest request;


  @Test
  public void testAddNewTroop() {
    String username = "testUser";
    Player player = new Player();
    player.setId(1L);
    player.setPlayerName(username);
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setPlayer(player);
    Troop newTroop = new Troop();
    newTroop.setId(1L);
    newTroop.setType("Infantry");
    newTroop.setKingdom(kingdom);

    TroopDto newTroopDto = new TroopDto();
    newTroopDto.setType("Infantry");

    when(authentication.getName()).thenReturn("user1");
    when(playerService.getPlayerByPlayerName("user1")).thenReturn(player);
    when(kingdomService.getKingdomByPlayerId(1L)).thenReturn(kingdom);
    when(buildingService.isAcademyBuildingPresent(kingdom)).thenReturn(true);
    when(troopService.convertDtoToTroop(newTroopDto)).thenReturn(newTroop);
    when(troopService.convertTroopToDto(newTroop)).thenReturn(newTroopDto);

    ResponseEntity<TroopDto> response = troopController.addNewTroop(authentication, newTroopDto,
        request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(newTroopDto, response.getBody());
    assertEquals(1, kingdom.getTroops().size());
  }

  @Test
  public void testGetKingdomTroops() {
    String username = "testUser";
    Player player = new Player();
    player.setId(1L);
    player.setPlayerName(username);
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setPlayer(player);
    Troop troop = new Troop();
    troop.setId(1L);
    troop.setType("Infantry");
    kingdom.getTroops().add(troop);
    TroopDto troopDto = new TroopDto();
    List<TroopDto> expectedTroopDtos = new ArrayList<>();
    expectedTroopDtos.add(troopDto);

    Authentication authentication = mock(Authentication.class);
    when(authentication.getName()).thenReturn(username);
    when(playerService.getPlayerByPlayerName(eq(username))).thenReturn(player);
    when(kingdomService.getKingdomByPlayerId(eq(player.getId()))).thenReturn(kingdom);
    when(troopService.listAllTroopsDto(kingdom)).thenReturn(expectedTroopDtos);

    ResponseEntity<List<TroopDto>> response = troopController.getKingdomTroops(authentication,
        request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedTroopDtos, response.getBody());
  }


}




