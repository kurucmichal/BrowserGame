package com.greenfoxacademy.aureuscctribesbackend.controllers;

import com.greenfoxacademy.aureuscctribesbackend.dtos.ResourcesDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.services.KingdomService;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.services.ResourceService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class ResourceController {

  private final KingdomService kingdomService;
  private final PlayerService playerService;
  private final ResourceService resourceService;
  private final LoggingService loggingService;


  @Autowired
  public ResourceController(KingdomService kingdomService, PlayerService playerService,
      ResourceService resourceService, LoggingService loggingService) {
    this.kingdomService = kingdomService;
    this.playerService = playerService;
    this.resourceService = resourceService;
    this.loggingService = loggingService;
  }

  @GetMapping("/kingdom/resources")
  public ResponseEntity getResourcesByKingdom(Authentication authentication,
      HttpServletRequest request) {

    String username = authentication.getName();
    Player player = playerService.getPlayerByPlayerName(username);
    Kingdom kingdom = kingdomService.getKingdomByPlayer(player);
    List<ResourcesDto> resources = resourceService.getResourcesDtoFromKingdom(kingdom);
    loggingService.logInfo(HttpStatus.OK.value(), "Resources displayed", request);
    return ResponseEntity.ok(resources);
  }
}
