package com.greenfoxacademy.aureuscctribesbackend.services.schedulers;

import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import com.greenfoxacademy.aureuscctribesbackend.repositories.KingdomRepository;
import com.greenfoxacademy.aureuscctribesbackend.services.ResourceService;
import com.greenfoxacademy.aureuscctribesbackend.services.TroopService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FoodConsumption {

  private final KingdomRepository kingdomRepository;


  private final TroopService troopService;

  private final ResourceService resourceService;

  @Autowired
  public FoodConsumption(KingdomRepository kingdomRepository, TroopService troopService,
      ResourceService resourceService) {
    this.kingdomRepository = kingdomRepository;
    this.troopService = troopService;
    this.resourceService = resourceService;
  }

  @Scheduled(fixedRate = 5000)
  @Async
  public void subtractFood() {
    List<Kingdom> kingdoms = kingdomRepository.findAll();
    for (Kingdom kingdom : kingdoms) {
      int totalFoodConsumption = 0;
      List<TroopDto> troops = troopService.listAllTroopsDto(kingdom);
      for (TroopDto troop : troops) {
        if (troop.getFinishedAt().isBefore(LocalDateTime.now())) {

          int foodConsumption = calculateFoodPerMinute(troop.getLevel());
          totalFoodConsumption += foodConsumption;
        }
      }

      int food = resourceService.getFood(kingdom).getQuantity();
      if (food >= totalFoodConsumption) {
        int food1 = resourceService.getFood(kingdom).getQuantity() - totalFoodConsumption;
        Food foodObj = resourceService.getFood(kingdom);
        foodObj.setQuantity(food1);
        resourceService.saveFood(foodObj);
        kingdomRepository.save(kingdom);
      }
    }
  }

  private int calculateFoodPerMinute(int level) {
    return level * 5;
  }
}
