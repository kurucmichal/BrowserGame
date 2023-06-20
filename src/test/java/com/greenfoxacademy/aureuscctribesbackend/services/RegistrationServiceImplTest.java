package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.dtos.AuthDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.LoginDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.UsernameAlreadyUsedException;
import com.greenfoxacademy.aureuscctribesbackend.models.ConfirmationToken;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.security.JwtService;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import io.jsonwebtoken.SignatureException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceImplTest {

  @Mock
  private PlayerRepository playerRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private KingdomService kingdomService;

  @Mock
  private ConfirmationTokenService confirmationTokenService;

  @InjectMocks
  private RegistrationServiceImpl registrationService;

  @Mock
  private PlayerService playerService;

  private LoginDto loginDto;

  private RegisterDto registerDto;


  @Before
  public void setUp() {
    loginDto = new LoginDto();
    loginDto.setUsername("testUser");
    loginDto.setPassword("testPassword");

    registerDto = new RegisterDto();
    registerDto.setUsername("testUser2");
    registerDto.setPassword("testPassword2");
    registerDto.setEmail("testUser2@example.com");
    registerDto.setKingdomName("testKingdom");
  }

  @Test(expected = BadCredentialsException.class)
  public void testLoginWithUnverifiedPlayer() {
    Player player = new Player();
    player.setPlayerName("testUser");
    player.setPassword(passwordEncoder.encode("testPassword"));
    player.setRoleType(RoleType.USER);
    player.setVerified(false);

    Mockito.when(playerRepository.findByPlayerName("testUser")).thenReturn(Optional.of(player));

    registrationService.login(loginDto);
  }

  @Test
  public void testSuccessfulLogin() {
    Player player = new Player();
    player.setPlayerName("testUser");
    player.setPassword(passwordEncoder.encode("testPassword"));
    player.setRoleType(RoleType.USER);
    player.setVerified(true);

    Mockito.when(playerRepository.findByPlayerName("testUser")).thenReturn(Optional.of(player));

    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("testUser",
        "testPassword");
    Mockito.when(
            authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(token);
    Mockito.when(jwtService.generateToken(player)).thenReturn("testToken");

    AuthDto result = registrationService.login(loginDto);
    assertNotNull(result);
    assertNotNull(result.getToken());
    assertEquals("testToken", result.getToken());

    ArgumentCaptor<UsernamePasswordAuthenticationToken> authenticationTokenCaptor =
        ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
    Mockito.verify(authenticationManager).authenticate(authenticationTokenCaptor.capture());
    UsernamePasswordAuthenticationToken authenticationToken = authenticationTokenCaptor.getValue();
    assertEquals("testUser", authenticationToken.getPrincipal());
    assertEquals("testPassword", authenticationToken.getCredentials());
  }

  @Test(expected = SignatureException.class)
  public void testLoginWithInvalidUsernameOrPassword() {
    Mockito.when(playerRepository.findByPlayerName("testUser")).thenReturn(Optional.empty());

    registrationService.login(loginDto);
  }

  @Test(expected = UsernameAlreadyUsedException.class)
  public void testRegisterWithExistingUsername() {
    Mockito.when(playerService.usernameExists("testUser2")).thenReturn(true);

    registrationService.register(registerDto);
  }

  @Test(expected = UsernameAlreadyUsedException.class)
  public void testRegisterWithExistingEmail() {
    Mockito.when(playerService.usernameExists("testUser2")).thenReturn(false);
    Mockito.when(playerService.emailExists("testUser2@example.com")).thenReturn(true);

    registrationService.register(registerDto);
  }

  @Test(expected = UsernameAlreadyUsedException.class)
  public void testRegisterWithExistingKingdomName() {
    Mockito.when(playerService.usernameExists("testUser2")).thenReturn(false);
    Mockito.when(playerService.emailExists("testUser2@example.com")).thenReturn(false);
    Mockito.when(kingdomService.kingdomExist("testKingdom")).thenReturn(true);

    registrationService.register(registerDto);
  }

  @Test
  public void testConfirmRegistration() {

    Player player = new Player();
    player.setId(1L);
    player.setPlayerName("testUser");
    player.setEmail("testEmail@test.com");
    player.setPassword("testPassword");
    player.setRoleType(RoleType.USER);
    player.setVerified(false);
    Mockito.doNothing().when(playerService).addPlayer(Mockito.any(Player.class));

    ConfirmationToken confirmationToken = new ConfirmationToken();
    confirmationToken.setId(1L);
    confirmationToken.setToken("testToken");
    confirmationToken.setPlayer(player);
    confirmationToken.setExpiryDate(LocalDateTime.now().plusDays(1));

    Mockito.when(confirmationTokenService.getConfirmationToken("testToken"))
        .thenReturn(confirmationToken);

    Player result = registrationService.confirmRegistration("testToken");

    assertNotNull(result);
    assertEquals(player.getId(), result.getId());
    assertEquals(player.getPlayerName(), result.getPlayerName());
    assertEquals(player.getEmail(), result.getEmail());
    assertEquals(player.getPassword(), result.getPassword());
    assertEquals(player.getRoleType(), result.getRoleType());
    assertTrue(result.isVerified());

    Mockito.verify(confirmationTokenService, Mockito.times(1))
        .deleteConfirmationToken(confirmationToken);
  }

  @Test
  public void testInvalidToken() {
    String invalidToken = "invalidToken";
    Mockito.when(confirmationTokenService.getConfirmationToken(invalidToken)).thenReturn(null);

    Exception exception = assertThrows(ObjectNotFoundException.class, () -> {
      registrationService.confirmRegistration(invalidToken);
    });

    assertEquals("Invalid token", exception.getMessage());
  }
}