package com.greenfoxacademy.aureuscctribesbackend.repositories;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Location;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KingdomRepository extends JpaRepository<Kingdom, Long> {

  Optional<Kingdom> findByNameAndPlayer(String name, Player player);

  Optional<Kingdom> getKingdomByPlayer(Player player);

  Optional<Kingdom> findByLocation(Location location);

  Optional<Kingdom> findByPlayer(Player player);

  Optional<Kingdom> findByPlayerId(Long id);

  boolean existsByName(String name);
}
