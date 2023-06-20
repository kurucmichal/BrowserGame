package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomBuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

  private final KingdomRepository kingdomRepository;

  public LeaderboardServiceImpl(KingdomRepository kingdomRepository) {
    this.kingdomRepository = kingdomRepository;
  }

  @Override
  public List<KingdomBuildingDto> getBuildingLeaderboard() {
    List<Kingdom> kingdoms = kingdomRepository.findAll();
    Map<String, Integer> buildingCounts = new HashMap<>();
    for (Kingdom kingdom : kingdoms) {
      int count = kingdom.getBuildings().size();
      buildingCounts.put(kingdom.getName(), count);
    }
    List<Entry<String, Integer>> sortedEntries = new ArrayList<>(buildingCounts.entrySet());
    sortedEntries.sort(Entry.comparingByValue(Comparator.reverseOrder()));
    List<KingdomBuildingDto> leaderboard = new ArrayList<>();
    for (Entry<String, Integer> entry : sortedEntries) {
      leaderboard.add(new KingdomBuildingDto(entry.getKey(), entry.getValue()));
    }
    return leaderboard;
  }
}

