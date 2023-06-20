package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.ResourcesDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.NullArgumentException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ResourceInvalidException;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.FoodRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.GoldRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl implements ResourceService {

  private FoodRepository foodRepository;
  private GoldRepository goldRepository;

  @Autowired
  public ResourceServiceImpl(FoodRepository foodRepository, GoldRepository goldRepository) {
    this.foodRepository = foodRepository;
    this.goldRepository = goldRepository;
  }

  @Override
  public Gold saveGold(Gold gold) {

    if (gold.isValid()) {
      return goldRepository.save(gold);
    } else {
      throw new ResourceInvalidException("The resource quantity is invalid!");
    }

  }

  @Override
  public Food saveFood(Food food) {

    if (food.isValid()) {
      return foodRepository.save(food);
    } else {
      throw new ResourceInvalidException("The resource quantity is invalid!");
    }
  }

  @Override
  public Gold getGold(Kingdom kingdom) {

    Optional<Gold> optionalGold = goldRepository.findByKingdom(kingdom);

    if (optionalGold.isPresent()) {
      return optionalGold.get();
    } else {
      throw new NoSuchElementException("Gold not found for kingdom: " + kingdom.getName());
    }

  }

  @Override
  public Food getFood(Kingdom kingdom) {

    Optional<Food> optionalFood = foodRepository.findByKingdom(kingdom);

    if (optionalFood.isPresent()) {
      return optionalFood.get();
    } else {
      throw new NoSuchElementException("Food not found for kingdom: " + kingdom.getName());
    }

  }

  @Override
  public void updateResource(long kingdomId, int goldChange, int foodChange) {
    Gold gold = goldRepository.findByKingdomId(kingdomId);
    if (gold == null) {
      throw new NullArgumentException("Gold is null for kingdomID" + kingdomId);
    }
    gold.setQuantity(gold.getQuantity() - goldChange);
    goldRepository.save(gold);

    Food food = foodRepository.findByKingdomId(kingdomId);
    if (food == null) {
      throw new NullArgumentException("Food is null for kingdomID" + kingdomId);
    }
    food.setQuantity(food.getQuantity() - foodChange);
    foodRepository.save(food);
  }

  @Override
  public List<ResourcesDto> getResourcesDtoFromKingdom(Kingdom kingdom) {
    Food food = kingdom.getFood();
    Gold gold = kingdom.getGold();

    ResourcesDto foodResource = new ResourcesDto("food", food.getQuantity());
    ResourcesDto goldResource = new ResourcesDto("gold", gold.getQuantity());

    List<ResourcesDto> resources = new ArrayList<>();
    resources.add(foodResource);
    resources.add(goldResource);

    return resources;
  }
}
