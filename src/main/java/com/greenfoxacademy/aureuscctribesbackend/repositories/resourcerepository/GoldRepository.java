package com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldRepository extends ResourceRepository {

  Optional<Gold> findByKingdom(Kingdom kingdom);

  Gold findByKingdomId(long kingdomId);
}
