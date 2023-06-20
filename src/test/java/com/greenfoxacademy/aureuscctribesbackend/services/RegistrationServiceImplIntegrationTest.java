package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.dtos.AuthDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.LoginDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterResponseDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.ConfirmationToken;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.transaction.Transactional;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RegistrationServiceImplIntegrationTest {

  @Autowired
  private RegistrationServiceImpl registrationService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private ConfirmationTokenService confirmationTokenService;

  @Autowired
  private KingdomService kingdomService;

  @Test
  public void testValidLogin() {
    Player player = new Player("testUser");
    player.setPassword(passwordEncoder.encode("testPassword"));
    player.setRoleType(RoleType.USER);
    player.setEmail("test@test.com");
    player.setVerified(true);
    playerRepository.save(player);

    LoginDto loginDto = new LoginDto("testUser", "testPassword");

    AuthDto authDto = registrationService.login(loginDto);
    assertNotNull(authDto.getToken());
  }

  @Test
  public void testInvalidLogin() {
    Player player = new Player("testUser");
    player.setPassword(passwordEncoder.encode("testPassword"));
    player.setRoleType(RoleType.USER);
    player.setEmail("test@test.com");
    player.setVerified(true);
    playerRepository.save(player);

    LoginDto loginDto = new LoginDto("testUser", "wrongPassword");

    assertThrows(BadCredentialsException.class, () -> registrationService.login(loginDto));
  }

  @Test
  public void testConfirmRegistration() {
    Player player = new Player("testUser");
    player.setEmail("test@test.com");
    player.setPassword(passwordEncoder.encode("testPassword"));
    player.setRoleType(RoleType.USER);
    playerRepository.save(player);

    String token = UUID.randomUUID().toString();
    LocalDateTime expiryDate = LocalDateTime.now().plusDays(1);
    ConfirmationToken confirmationToken = new ConfirmationToken(token, player, expiryDate);
    confirmationTokenService.saveConfirmationToken(confirmationToken);

    Player confirmedPlayer = registrationService.confirmRegistration(token);

    assertTrue(confirmedPlayer.isVerified());
  }

  @Test
  public void testConfirmRegistrationWithInvalidToken() {
    assertThrows(ObjectNotFoundException.class, () -> {
      registrationService.confirmRegistration("invalid_token");
    });
  }

  @Test
  public void testRegister() {

    RegisterDto registerDto = new RegisterDto();
    registerDto.setUsername("testUser");
    registerDto.setPassword("testPassword");
    registerDto.setEmail("test@test.com");
    registerDto.setKingdomName("testKingdom");

    RegisterResponseDto responseDto = registrationService.register(registerDto);

    assertNotNull(responseDto.getToken());

    Player savedPlayer = playerRepository.findByPlayerName("testUser").get();
    assertNotNull(savedPlayer);
    assertEquals("test@test.com", savedPlayer.getEmail());
    assertEquals("testKingdom", savedPlayer.getKingdom().getName());

    assertNotEquals("testPassword", savedPlayer.getPassword());
    assertTrue(passwordEncoder.matches("testPassword", savedPlayer.getPassword()));

    ConfirmationToken savedToken = confirmationTokenService.getConfirmationToken(
        responseDto.getToken());
    assertNotNull(savedToken);
    assertEquals(savedPlayer, savedToken.getPlayer());

    Kingdom savedKingdom = kingdomService.getKingdomByPlayer(savedPlayer);
    assertNotNull(savedKingdom);
    assertEquals("testKingdom", savedPlayer.getKingdom().getName());
  }
}

