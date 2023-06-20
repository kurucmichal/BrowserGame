package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.factories.BuildingFactory;
import com.greenfoxacademy.aureuscctribesbackend.models.Academy;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.repositories.AcademyRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcademyServiceImpl implements ConcreteBuildingService {

  private final AcademyRepository repository;
  private final BuildingFactory factory = new BuildingFactory();

  @Autowired
  public AcademyServiceImpl(AcademyRepository repository) {
    this.repository = repository;
  }

  public Academy save(Academy academy) {
    return repository.save(academy);
  }

  public List<Academy> findAllByKingdomId(Kingdom kingdom) {
    return repository.findAllByKingdom(kingdom).get();
  }

  public List<BuildingDto> listAcademyDto(Kingdom kingdom) {

    List<Academy> academies = findAllByKingdomId(kingdom);
    List<BuildingDto> academyDtos = new ArrayList<>();

    for (Academy academy : academies) {
      academyDtos.add(convertBuildingToDto(academy));
    }

    return academyDtos;
  }

  @Override
  public BuildingDto convertBuildingToDto(Building building) {
    return new BuildingDto(building.getId(), building.getType(), building.getLevel(),
        building.getHp(), building.getStartedAt(), building.getFinishedAt());
  }

  @Override
  public Building create(Kingdom kingdom) {
    Academy academy = factory.createAcademy(kingdom);
    return repository.save(academy);
  }

}
