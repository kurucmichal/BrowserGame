package com.greenfoxacademy.aureuscctribesbackend.repositories;

import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

  Optional<Player> findByPlayerName(String playerName);

  Boolean existsByPlayerName(String username);

  Boolean existsByEmail(String email);
}
