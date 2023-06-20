package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Location;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import java.util.List;

public interface KingdomService {

  Kingdom saveKingdom(Kingdom kingdom);

  Kingdom findByLocation(Location location);

  Kingdom createNewKingdom(Player player);

  List<Kingdom> listAllKingdoms();

  Kingdom getKingdomByPlayerId(Long playerId);

  Kingdom getByPlayerIdOptional(Long playerId);

  Kingdom getKingdomByPlayer(Player player);

  KingdomDto convertKingdomToDto(Kingdom kingdom);

  List<KingdomDto> listAllKingdomDto();

  Kingdom createKingdom(String kingdomName, Player player);

  Boolean kingdomExist(String username);
}
