package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerNameNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;

public interface PlayerService {

  Player savePlayer(Player player);

  Player getPlayerByPlayerName(String playerName) throws PlayerNameNotFoundException;

  Boolean usernameExists(String username);

  Player getPlayerById(Long playerId);

  void addPlayer(Player player);

  Boolean emailExists(String email);
}
