package com.greenfoxacademy.aureuscctribesbackend.controllers;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomBuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.LeaderboardServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class BuildingLeaderboardControllerTestMvc {

  @Mock
  private KingdomServiceImpl kingdomService;

  @Mock
  private LeaderboardServiceImpl leaderboardService;

  @InjectMocks
  private BuildingLeaderboardController buildingLeaderboardController;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(buildingLeaderboardController).build();
  }

  @Test
  public void testGetBuildingLeaderboard() throws Exception {
    List<KingdomBuildingDto> expectedLeaderboard = new ArrayList<>();
    expectedLeaderboard.add(new KingdomBuildingDto("Kingdom A", 10));
    expectedLeaderboard.add(new KingdomBuildingDto("Kingdom B", 8));
    expectedLeaderboard.add(new KingdomBuildingDto("Kingdom C", 6));
    Mockito.when(leaderboardService.getBuildingLeaderboard()).thenReturn(expectedLeaderboard);

    mockMvc.perform(MockMvcRequestBuilders.get("/kingdoms/leaderboard/buildings"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].kingdomName", Matchers.is("Kingdom A")))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].buildingCount", Matchers.is(10)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].kingdomName", Matchers.is("Kingdom B")))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].buildingCount", Matchers.is(8)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[2].kingdomName", Matchers.is("Kingdom C")))
        .andExpect(MockMvcResultMatchers.jsonPath("$[2].buildingCount", Matchers.is(6)));
  }
}