package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;


public interface ConcreteBuildingService {

  Building create(Kingdom kingdom);

  BuildingDto convertBuildingToDto(Building building);

}
