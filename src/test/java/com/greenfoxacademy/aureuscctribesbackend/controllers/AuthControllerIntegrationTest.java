package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.repositories.ConfirmationTokenRepository;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.services.PlayerService;
import com.greenfoxacademy.aureuscctribesbackend.services.RegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private PlayerService playerService;

  @Autowired
  private ConfirmationTokenRepository confirmationTokenRepository;

  @Autowired
  private RegistrationService registrationService;


  @Test
  public void testRegistrationSuccess() {
    RegisterDto registerDto = new RegisterDto("testuser", "password", "testuser@test.com",
        "New Kingdom");

    ResponseEntity<RegisterDto> responseEntity =
        restTemplate.postForEntity("/api/auth/register", registerDto, RegisterDto.class,
            MediaType.APPLICATION_JSON);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    Player savedPlayer = playerRepository.findByPlayerName(registerDto.getUsername()).orElse(null);

    assertThat(savedPlayer).isNotNull();
    assert savedPlayer != null;
    assertThat(savedPlayer.getPlayerName()).isEqualTo(registerDto.getUsername());
    assertThat(savedPlayer.getPassword()).isNotEqualTo(registerDto.getPassword());
    assertThat(savedPlayer.getEmail()).isEqualTo(registerDto.getEmail());
    assertThat(savedPlayer.isVerified()).isFalse();

    Kingdom savedKingdom = savedPlayer.getKingdom();
    assertThat(savedKingdom).isNotNull();
    assertThat(savedKingdom.getName()).isEqualTo(registerDto.getKingdomName());
    assertThat(savedKingdom.getGold()).isNotNull();
    assertThat(savedKingdom.getFood()).isNotNull();
  }
}
