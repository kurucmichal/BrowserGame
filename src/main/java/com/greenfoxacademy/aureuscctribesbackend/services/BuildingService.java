package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.NoBuildingFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;

import java.util.List;

public interface BuildingService {

  Building save(Building building);


  Building create(Kingdom kingdom, String type);

  Building getById(Long id) throws NoBuildingFoundException;

  Building upgrade(Building building, int newLevel);

  int upgradeHp(int level, String buildingType);

  BuildingDto convertBuildingToDto(Building building);

  List<BuildingDto> listAllBuildingDto(Kingdom kingdom);

  boolean isBuildingPresent(Player player, Class<? extends Building> buildingType);


  boolean isAcademyBuildingPresent(Kingdom kingdom);
}
