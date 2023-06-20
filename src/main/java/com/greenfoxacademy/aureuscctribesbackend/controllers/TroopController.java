package com.greenfoxacademy.aureuscctribesbackend.controllers;

import com.greenfoxacademy.aureuscctribesbackend.dtos.ErrorResponseDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.MissingCriteriaException;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.services.BuildingService;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.services.PurchaseService;
import com.greenfoxacademy.aureuscctribesbackend.services.TroopService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/kingdom")
public class TroopController {

  private final KingdomService kingdomService;
  private final BuildingService buildingService;
  private final PlayerService playerService;
  private final TroopService troopService;
  private final LoggingService loggingService;
  private final PurchaseService purchaseService;

  @Autowired
  public TroopController(KingdomService kingdomService, BuildingService buildingService,
      PlayerService playerService, TroopService troopService, LoggingService loggingService,
      PurchaseService purchaseService) {
    this.kingdomService = kingdomService;
    this.buildingService = buildingService;
    this.playerService = playerService;
    this.troopService = troopService;
    this.loggingService = loggingService;
    this.purchaseService = purchaseService;
  }

  @GetMapping("/troops")
  public ResponseEntity getKingdomTroops(Authentication authentication,
      HttpServletRequest request) {
    String username = authentication.getName();
    Player player = playerService.getPlayerByPlayerName(username);
    Kingdom kingdom = kingdomService.getKingdomByPlayerId(player.getId());
    if (kingdom == null) {
      loggingService.logError(HttpStatus.NOT_FOUND.value(), "Kingdom not found", request);
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ErrorResponseDto("error", "Kingdom not found"));
    }
    loggingService.logInfo(HttpStatus.OK.value(), "Troops displayed", request);
    List<TroopDto> troopDtos = troopService.listAllTroopsDto(kingdom);
    return ResponseEntity.ok(troopDtos);
  }

  @PostMapping("/troop")
  public ResponseEntity addNewTroop(Authentication authentication,
      @RequestBody TroopDto newTroopDto, HttpServletRequest request) {
    String username = authentication.getName();
    Player player = playerService.getPlayerByPlayerName(username);
    Kingdom kingdom = kingdomService.getKingdomByPlayerId(player.getId());
    if (kingdom == null) {
      loggingService.logError(HttpStatus.NOT_FOUND.value(), "Kingdom not found", request);
      return ResponseEntity.notFound().build();
    }
    if (buildingService.isAcademyBuildingPresent(kingdom)) {
      Troop newTroop = troopService.convertDtoToTroop(newTroopDto);
      newTroop.setKingdom(kingdom);
      troopService.saveTroop(newTroop);
      kingdom.getTroops().add(newTroop);
      kingdomService.saveKingdom(kingdom);
      TroopDto troopDto = troopService.convertTroopToDto(newTroop);
      loggingService.logInfo(HttpStatus.OK.value(), "Troop added to kingdom", request);
      return ResponseEntity.ok(troopDto);
    } else {
      loggingService.logError(HttpStatus.BAD_REQUEST.value(), "No academy in kingdom", request);
      throw new MissingCriteriaException("Kingdom: '" + kingdom.getName()
          + "' does not meet criteria to post Troop: 'Academy is missing'");
    }
  }

  @PutMapping("/troop/{lvl}")
  public ResponseEntity<List<TroopDto>> upgradeTroops(@PathVariable("lvl") int level,
      Authentication authentication,
      HttpServletRequest request) {

    String username = authentication.getName();
    Player player = playerService.getPlayerByPlayerName(username);
    Kingdom kingdom = kingdomService.getKingdomByPlayer(player);

    List<Troop> troopsWithLevel = troopService.listTroopsByLevel(level, kingdom);
    troopsWithLevel.forEach(troop -> {
      purchaseService.upgradeTroop(troop);
      loggingService.logInfo(HttpStatus.OK.value(),
          "Troop with ID `" + troop.getId() + "` upgraded to level `" + troop.getLevel() + "`",
          request);
    });

    List<TroopDto> troopDtos = troopService.listAllTroopsDto(kingdom);
    loggingService.logInfo(HttpStatus.OK.value(),
        "listing all troops of kingdom: " + kingdom.getName(), request);
    return ResponseEntity.ok().body(troopDtos);
  }
}

