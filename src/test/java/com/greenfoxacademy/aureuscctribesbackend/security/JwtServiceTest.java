package com.greenfoxacademy.aureuscctribesbackend.security;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import java.lang.reflect.Method;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtServiceTest {

  @Test
  public void testGenerateToken() {
    Player mockPlayer = mock(Player.class);
    when(mockPlayer.getPlayerName()).thenReturn("testUser");
    JwtService jwtService = new JwtService();
    String token = jwtService.generateToken(mockPlayer);
    assertNotNull(token);
    String[] tokenParts = token.split("\\.");
    assertEquals(3, tokenParts.length);
    assertTrue(tokenParts[0].length() > 0);
    assertTrue(tokenParts[1].length() > 0);
    assertTrue(tokenParts[2].length() > 0);
  }

  @Test
  public void testExtractUsername() {
    JwtService jwtService = new JwtService();
    Player mockPlayer = mock(Player.class);
    when(mockPlayer.getPlayerName()).thenReturn("User");
    String token = jwtService.generateToken(mockPlayer);
    String username = jwtService.extractUsername(token);
    assertEquals(mockPlayer.getPlayerName(), username);
  }

  @Test
  public void testIsTokenValidWithValidToken() {
    JwtService jwtService = new JwtService();
    Player mockPlayer = mock(Player.class);
    when(mockPlayer.getPlayerName()).thenReturn("testUser");
    String token = jwtService.generateToken(mockPlayer);
    UserDetails userDetails = new User("testUser", "", new ArrayList<>());

    boolean result = jwtService.isTokenValid(token, userDetails);

    assertTrue(result);
  }

  @Test
  public void testIsTokenValidWithInvalidUserDetails() {
    JwtService jwtService = new JwtService();
    Player mockPlayer = mock(Player.class);
    when(mockPlayer.getPlayerName()).thenReturn("testUser");
    String token = jwtService.generateToken(mockPlayer);
    UserDetails userDetails = new User("invalidUser", "", new ArrayList<>());

    boolean result = jwtService.isTokenValid(token, userDetails);

    assertFalse(result);
  }

  @Test
  public void testIsTokenExpired() throws Exception {
    JwtService jwtService = new JwtService();
    Player mockPlayer = mock(Player.class);
    when(mockPlayer.getPlayerName()).thenReturn("testUser");
    String token = jwtService.generateToken(mockPlayer);

    Method isTokenExpiredMethod = JwtService.class.getDeclaredMethod("isTokenExpired",
        String.class);
    isTokenExpiredMethod.setAccessible(true);

    boolean result = (boolean) isTokenExpiredMethod.invoke(jwtService, token);

    assertFalse(result);
  }
}