package com.greenfoxacademy.aureuscctribesbackend.controllers;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RequestNewBuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.services.AcademyServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.BuildingService;
import com.greenfoxacademy.aureuscctribesbackend.services.FarmServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.MineServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.services.PurchaseServiceImpl;
import com.greenfoxacademy.aureuscctribesbackend.services.TownHallServiceImpl;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuildingController {

  private final KingdomService kingdomService;
  private final FarmServiceImpl farmService;
  private final AcademyServiceImpl academyService;
  private final MineServiceImpl mineService;
  private final TownHallServiceImpl townHallService;
  private final PlayerService playerService;
  private final BuildingService buildingService;
  private final PurchaseServiceImpl purchaseService;
  private final LoggingService loggingService;


  @Autowired
  public BuildingController(KingdomService kingdomService,
      FarmServiceImpl farmService, AcademyServiceImpl academyService, MineServiceImpl mineService,
      TownHallServiceImpl townHallService, PlayerService playerService,
      BuildingService buildingService, PurchaseServiceImpl purchaseService,
      LoggingService loggingService) {

    this.kingdomService = kingdomService;
    this.farmService = farmService;
    this.academyService = academyService;
    this.mineService = mineService;
    this.townHallService = townHallService;
    this.playerService = playerService;
    this.buildingService = buildingService;
    this.purchaseService = purchaseService;
    this.loggingService = loggingService;
  }

  @GetMapping("/kingdom/buildings")
  public ResponseEntity<List<BuildingDto>> getKingdomBuildings(Authentication authentication,
      HttpServletRequest request) {

    String username = authentication.getName();
    Player player = playerService.getPlayerByPlayerName(username);
    Kingdom kingdom = kingdomService.getByPlayerIdOptional(player.getId());

    List<BuildingDto> buildingsDtolist = buildingService.listAllBuildingDto(kingdom);
    loggingService.logInfo(HttpStatus.OK.value(), "All buildings displayed", request);
    return ResponseEntity.ok(buildingsDtolist);
  }

  @GetMapping("/kingdom/buildings/{buildingname}")
  public ResponseEntity getKingdomBuildingsByName(Authentication authentication,
      @PathVariable(name = "buildingname") String buildingName, HttpServletRequest request) {

    String username = authentication.getName();
    Player player = playerService.getPlayerByPlayerName(username);
    Kingdom kingdom = kingdomService.getByPlayerIdOptional(player.getId());

    List<BuildingDto> buildingDtos;

    switch (buildingName.toLowerCase()) {
      case "farm":
        buildingDtos = farmService.listAllFarmDto(kingdom);
        loggingService.logInfo(HttpStatus.OK.value(), "All farms displayed", request);
        return ResponseEntity.ok(buildingDtos);
      case "mine":
        buildingDtos = mineService.listAllMineDto(kingdom);
        loggingService.logInfo(HttpStatus.OK.value(), "All mines displayed", request);
        return ResponseEntity.ok(buildingDtos);
      case "townhall":
        buildingDtos = townHallService.listTownHallDto(kingdom);
        loggingService.logInfo(HttpStatus.OK.value(), "All townhalls displayed", request);
        return ResponseEntity.ok(buildingDtos);
      case "academy":
        buildingDtos = academyService.listAcademyDto(kingdom);
        loggingService.logInfo(HttpStatus.OK.value(), "All academies displayed", request);
        return ResponseEntity.ok(buildingDtos);
      default:
        throw new ObjectNotFoundException("No criteria met for parameter: " + buildingName);
    }
  }

  @PostMapping("/kingdom/building")
  public ResponseEntity addNewBuilding(Authentication authentication,
      @Valid @RequestBody RequestNewBuildingDto newBuildingRequestDto) {

    String username = authentication.getName();
    Player player = playerService.getPlayerByPlayerName(username);
    Kingdom kingdom = kingdomService.getKingdomByPlayerId(player.getId());

    Building newBuilding = purchaseService.purchaseBuilding(kingdom,
        newBuildingRequestDto.getType());
    BuildingDto newBuildingDto = buildingService.convertBuildingToDto(newBuilding);
    return ResponseEntity.ok(newBuildingDto);
  }
}
