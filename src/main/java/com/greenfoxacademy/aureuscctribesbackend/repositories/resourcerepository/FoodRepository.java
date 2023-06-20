package com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends ResourceRepository {

  Optional<Food> findByKingdom(Kingdom kingdom);


  Food findByKingdomId(long kingdomId);
}
