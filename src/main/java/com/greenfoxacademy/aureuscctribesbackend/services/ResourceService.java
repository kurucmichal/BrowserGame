package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.ResourcesDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import java.util.List;

public interface ResourceService {

  public Gold saveGold(Gold gold);

  public Food saveFood(Food food);

  public Gold getGold(Kingdom kingdom);

  public Food getFood(Kingdom kingdom);

  void updateResource(long kingdomId, int goldChange, int foodChange);

  List<ResourcesDto> getResourcesDtoFromKingdom(Kingdom kingdom);
}
