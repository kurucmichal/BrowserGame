package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ResourceInvalidException;
import com.greenfoxacademy.aureuscctribesbackend.models.Farm;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.services.AcademyServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.BuildingServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.FarmServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.MineServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.services.PurchaseServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.ResourceService;
import com.greenfoxacademy.aureuscctribesbackend.services.TownHallServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.procedure.NoSuchParameterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;


@WebMvcTest(BuildingController.class)
@RunWith(SpringRunner.class)
public class BuildingControllerTest {

  @MockBean
  private KingdomService kingdomService;

  @MockBean
  private FarmServiceImpl farmService;

  @MockBean
  private BuildingServiceImpl buildingService;
  @MockBean
  private AcademyServiceImpl academyService;

  @MockBean
  private MineServiceImpl mineService;

  @MockBean
  private TownHallServiceImpl townHallService;

  @MockBean
  private PlayerService playerService;

  @MockBean
  private PurchaseServiceImpl purchaseService;

  @MockBean
  private ResourceService resourceService;

  @MockBean
  private LoggingService loggingService;

  @Autowired
  private MockMvc mockMvc;

  @WithMockUser(value = "TestUser")
  @Test
  public void testGetKingdomBuildings() throws Exception {
    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);

    BuildingDto farmDTO = new BuildingDto();
    farmDTO.setId(1L);
    farmDTO.setType("farm");
    farmDTO.setLevel(1);
    farmDTO.setHp(100);

    BuildingDto farmDTO2 = new BuildingDto();
    farmDTO2.setId(2L);
    farmDTO2.setType("farm");
    farmDTO2.setLevel(1);
    farmDTO2.setHp(100);

    List<BuildingDto> buildingDtos = new ArrayList<>();
    buildingDtos.add(farmDTO);
    buildingDtos.add(farmDTO2);

    when(kingdomService.getByPlayerIdOptional(player.getId())).thenReturn(kingdom);
    when(playerService.getPlayerByPlayerName(any())).thenReturn(player);
    when(buildingService.listAllBuildingDto(any())).thenReturn(buildingDtos);

    mockMvc.perform(get("/kingdom/buildings"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].type", is("farm")))
        .andExpect(jsonPath("$[0].level", is(1)))
        .andExpect(jsonPath("$[0].hp", is(100)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].type", is("farm")))
        .andExpect(jsonPath("$[1].level", is(1)))
        .andExpect(jsonPath("$[1].hp", is(100)))
        .andDo(print());

  }

  @WithMockUser(value = "TestUser")
  @Test
  public void testGetKingdomBuildingsNotFound() throws Exception {

    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);

    when(kingdomService.getByPlayerIdOptional(player.getId())).thenThrow(ObjectNotFoundException.class);
    when(playerService.getPlayerByPlayerName(any())).thenReturn(player);

    mockMvc.perform(get("/kingdom/buildings"))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @WithMockUser(value = "TestUser")
  @Test
  public void testGetKingdomBuildingsByName() throws Exception {

    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);

    BuildingDto farmDTO = new BuildingDto();
    farmDTO.setId(1L);
    farmDTO.setType("farm");
    farmDTO.setLevel(1);
    farmDTO.setHp(100);

    BuildingDto farmDTO2 = new BuildingDto();
    farmDTO2.setId(2L);
    farmDTO2.setType("farm");
    farmDTO2.setLevel(1);
    farmDTO2.setHp(100);

    List<BuildingDto> buildingDtos = new ArrayList<>();
    buildingDtos.add(farmDTO);
    buildingDtos.add(farmDTO2);

    when(kingdomService.getByPlayerIdOptional(player.getId())).thenReturn(kingdom);
    when(playerService.getPlayerByPlayerName(any())).thenReturn(player);
    when(farmService.listAllFarmDto(any())).thenReturn(buildingDtos);

    mockMvc.perform(get("/kingdom/buildings/farm"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].type", is("farm")))
        .andExpect(jsonPath("$[0].level", is(1)))
        .andExpect(jsonPath("$[0].hp", is(100)))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].type", is("farm")))
        .andExpect(jsonPath("$[1].level", is(1)))
        .andExpect(jsonPath("$[1].hp", is(100)))
        .andDo(print());

  }

  @WithMockUser(value = "TestUser")
  @Test
  public void testGetKingdomBuildingsByNameNotFound() throws Exception {

    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);

    when(kingdomService.getByPlayerIdOptional(player.getId())).thenReturn(kingdom);
    when(playerService.getPlayerByPlayerName(any())).thenReturn(player);

    mockMvc.perform(get("/kingdom/buildings/something"))
        .andExpect(status().isNotFound())
        .andDo(print());

  }

  @WithMockUser(value = "TestUser")
  @Test
  public void testGetKingdomBuildingsByNameKingdomNotFound() throws Exception {

    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);

    when(kingdomService.getByPlayerIdOptional(player.getId())).thenThrow(ObjectNotFoundException.class);
    when(playerService.getPlayerByPlayerName(any())).thenReturn(player);

    mockMvc.perform(get("/kingdom/buildings/farm"))
        .andExpect(status().isNotFound())
        .andDo(print());

  }

  @WithMockUser(value = "TestUser")
  @Test
  public void addNewBuildingTest() throws Exception {

    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);

    BuildingDto farmDTO = new BuildingDto();
    farmDTO.setType("farm");
    farmDTO.setLevel(1);
    farmDTO.setHp(100);

    Farm farm = new Farm(kingdom);

    when(kingdomService.getKingdomByPlayerId(player.getId())).thenReturn(kingdom);
    when(playerService.getPlayerByPlayerName(any())).thenReturn(player);
    when(purchaseService.purchaseBuilding(any(), any())).thenReturn(farm);
    when(buildingService.convertBuildingToDto(any())).thenReturn(farmDTO);

    mockMvc.perform(post("/kingdom/building")
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

  @WithMockUser(value = "TestUser")
  @Test
  public void addNewBuildingTestNotEnoughResources() throws Exception {

    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);

    BuildingDto farmDTO = new BuildingDto();
    farmDTO.setType("farm");
    farmDTO.setLevel(1);
    farmDTO.setHp(100);

    Farm farm = new Farm(kingdom);

    when(kingdomService.getKingdomByPlayerId(player.getId())).thenReturn(kingdom);
    when(playerService.getPlayerByPlayerName(any())).thenReturn(player);
    when(purchaseService.purchaseBuilding(any(), any())).thenThrow(ResourceInvalidException.class);
    when(buildingService.convertBuildingToDto(any())).thenReturn(farmDTO);

    mockMvc.perform(post("/kingdom/building")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"type\" : \"farm\"}")
            .accept(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("400 - Bad Request")))
        .andDo(print());
  }

  @WithMockUser(value = "TestUser")
  @Test
  public void addNewBuildingTestNosuchBuildingType() throws Exception {

    Player player = new Player();
    player.setId(1L);
    Kingdom kingdom = new Kingdom();
    kingdom.setPlayer(player);
    player.setKingdom(kingdom);

    BuildingDto farmDTO = new BuildingDto();
    farmDTO.setType("farm");
    farmDTO.setLevel(1);
    farmDTO.setHp(100);

    Farm farm = new Farm(kingdom);

    when(kingdomService.getKingdomByPlayerId(player.getId())).thenReturn(kingdom);
    when(playerService.getPlayerByPlayerName(any())).thenReturn(player);
    when(purchaseService.purchaseBuilding(any(), any())).thenThrow(NoSuchParameterException.class);
    when(buildingService.convertBuildingToDto(any())).thenReturn(farmDTO);

    mockMvc.perform(post("/kingdom/building")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"type\" : \"farm\"}")
            .accept(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("400 - Bad Request")))
        .andDo(print());
  }

}