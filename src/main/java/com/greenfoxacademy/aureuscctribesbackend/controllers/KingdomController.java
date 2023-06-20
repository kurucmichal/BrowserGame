package com.greenfoxacademy.aureuscctribesbackend.controllers;

import com.greenfoxacademy.aureuscctribesbackend.dtos.KingdomDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KingdomController {

  private final KingdomService kingdomService;
  private final PlayerService playerService;
  private final LoggingService loggingService;

  @Autowired
  public KingdomController(KingdomService kingdomService, PlayerService playerService,
      LoggingService loggingService) {
    this.kingdomService = kingdomService;
    this.playerService = playerService;
    this.loggingService = loggingService;
  }

  @GetMapping("/kingdoms")
  public ResponseEntity<List<KingdomDto>> getKingdoms(HttpServletRequest request) {
    List<KingdomDto> kingdomDtos = kingdomService.listAllKingdomDto();
    if (kingdomDtos.isEmpty()) {
      loggingService.logInfo(HttpStatus.OK.value(), "No kingdom was found", request);
      return ResponseEntity.ok(Collections.emptyList());
    } else {
      loggingService.logInfo(HttpStatus.OK.value(), "All kingdoms displayed", request);
      return ResponseEntity.ok(kingdomDtos);
    }
  }

  @GetMapping("/kingdom/{id}")
  public ResponseEntity getKingdomById(@PathVariable Long id, HttpServletRequest request) {

    playerService.getPlayerById(id);
    Kingdom kingdom = kingdomService.getKingdomByPlayerId(id);
    KingdomDto kingdomDto = kingdomService.convertKingdomToDto(kingdom);
    loggingService.logInfo(HttpStatus.OK.value(), "Kingdom displayed", request);
    return ResponseEntity.ok(kingdomDto);
  }
}
