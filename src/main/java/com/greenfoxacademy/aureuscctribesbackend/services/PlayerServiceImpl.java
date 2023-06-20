package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerIdNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerNameNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

  private final PlayerRepository playerRepository;

  @Autowired
  public PlayerServiceImpl(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public void addPlayer(Player player) {
    playerRepository.save(player);
  }

  @Override
  public Player savePlayer(Player player) {
    return Optional.ofNullable(playerRepository.save(player))
        .orElseThrow(() -> new RuntimeException("Failed to save Kingdom"));
  }

  @Override
  public Player getPlayerByPlayerName(String playerName) throws PlayerNameNotFoundException {
    Optional<Player> player = playerRepository.findByPlayerName(playerName);
    if (player.isPresent()) {
      return player.get();
    } else {
      throw new PlayerNameNotFoundException("Player name not found: " + playerName);
    }
  }

  @Override
  public Boolean usernameExists(String username) {
    return playerRepository.existsByPlayerName(username);
  }

  @Override
  public Boolean emailExists(String email) {
    return playerRepository.existsByEmail(email);
  }

  @Override
  public Player getPlayerById(Long playerId) {
    Optional<Player> player = playerRepository.findById(playerId);
    if (player.isPresent()) {
      return player.get();
    } else {
      throw new PlayerIdNotFoundException("Player id not found" + playerId);
    }
  }
}


