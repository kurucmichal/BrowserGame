package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.factories.BuildingFactory;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Mine;
import com.greenfoxacademy.aureuscctribesbackend.repositories.MineRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MineServiceImpl implements ConcreteBuildingService {

  private final MineRepository repository;
  private final BuildingFactory factory = new BuildingFactory();

  @Autowired
  public MineServiceImpl(MineRepository repository) {
    this.repository = repository;
  }

  public Mine save(Mine mine) {
    return repository.save(mine);
  }

  public List<Mine> findAllByKingdomId(Kingdom kingdom) {
    return repository.findAllByKingdom(kingdom).get();
  }

  @Override
  public Building create(Kingdom kingdom) {
    Mine mine = factory.createMine(kingdom);
    return repository.save(mine);
  }

  public List<BuildingDto> listAllMineDto(Kingdom kingdom) {

    List<Mine> mines = findAllByKingdomId(kingdom);
    List<BuildingDto> mineDtos = new ArrayList<>();

    for (Mine mine : mines) {
      mineDtos.add(convertBuildingToDto(mine));
    }

    return mineDtos;
  }

  @Override
  public BuildingDto convertBuildingToDto(Building building) {
    return new BuildingDto(building.getId(), building.getType(), building.getLevel(),
        building.getHp(), building.getStartedAt(), building.getFinishedAt());
  }

}
