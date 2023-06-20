package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomBuildingDto;
import java.util.List;

public interface LeaderboardService {

  public List<KingdomBuildingDto> getBuildingLeaderboard();

}
