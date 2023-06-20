package com.greenfoxacademy.aureuscctribesbackend.controllers;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomBuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LeaderboardServiceImpl;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kingdoms")
public class BuildingLeaderboardController {


  private final KingdomService kingdomService;

  private final LeaderboardServiceImpl leaderboardService;

  public BuildingLeaderboardController(KingdomService kingdomService,
      LeaderboardServiceImpl leaderboardService) {
    this.kingdomService = kingdomService;
    this.leaderboardService = leaderboardService;
  }

  @GetMapping("/leaderboard/buildings")
  public ResponseEntity<List<KingdomBuildingDto>> getBuildingLeaderboard() {
    List<KingdomBuildingDto> leaderboard = leaderboardService.getBuildingLeaderboard();
    return ResponseEntity.ok(leaderboard);
  }

}

