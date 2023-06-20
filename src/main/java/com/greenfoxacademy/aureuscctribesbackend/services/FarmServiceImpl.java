package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.factories.BuildingFactory;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Farm;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.repositories.FarmRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FarmServiceImpl implements ConcreteBuildingService {

  private final FarmRepository repository;
  private final BuildingFactory factory = new BuildingFactory();

  @Autowired
  public FarmServiceImpl(FarmRepository repository) {
    this.repository = repository;
  }

  public Farm save(Farm farm) {
    return repository.save(farm);
  }

  public List<Farm> findAllByKingdomId(Kingdom kingdom) {
    return repository.findAllByKingdom(kingdom).get();
  }

  @Override
  public BuildingDto convertBuildingToDto(Building building) {
    return new BuildingDto(building.getId(), building.getType(), building.getLevel(),
        building.getHp(), building.getStartedAt(), building.getFinishedAt());
  }

  public List<BuildingDto> listAllFarmDto(Kingdom kingdom) {

    List<Farm> farms = findAllByKingdomId(kingdom);
    List<BuildingDto> farmDtos = new ArrayList<>();

    for (Farm farm : farms) {
      farmDtos.add(convertBuildingToDto(farm));
    }

    return farmDtos;
  }

  @Override
  public Building create(Kingdom kingdom) {
    Farm farm = factory.createFarm(kingdom);
    return repository.save(farm);
  }
}
