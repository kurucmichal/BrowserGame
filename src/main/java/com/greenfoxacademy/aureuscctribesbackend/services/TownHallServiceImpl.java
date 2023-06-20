package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.factories.BuildingFactory;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.TownHall;
import com.greenfoxacademy.aureuscctribesbackend.repositories.TownHallRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TownHallServiceImpl implements ConcreteBuildingService {

  private final TownHallRepository repository;
  private final BuildingFactory factory = new BuildingFactory();

  @Autowired
  public TownHallServiceImpl(TownHallRepository repository) {
    this.repository = repository;
  }

  public TownHall save(TownHall townHall) {
    return repository.save(townHall);
  }

  public List<TownHall> findAllByKingdomId(Kingdom kingdom) {
    return repository.findAllByKingdom(kingdom).get();
  }

  public List<BuildingDto> listTownHallDto(Kingdom kingdom) {

    List<TownHall> townHalls = findAllByKingdomId(kingdom);
    List<BuildingDto> buildingDtos = new ArrayList<>();

    for (TownHall townHall : townHalls) {
      buildingDtos.add(convertBuildingToDto(townHall));
    }

    return buildingDtos;
  }

  @Override
  public BuildingDto convertBuildingToDto(Building building) {
    return new BuildingDto(building.getId(), building.getType(), building.getLevel(),
        building.getHp(), building.getStartedAt(), building.getFinishedAt());
  }

  @Override
  public Building create(Kingdom kingdom) {
    TownHall townHall = factory.createTownHall(kingdom);
    return repository.save(townHall);
  }


}
