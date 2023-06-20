package com.greenfoxacademy.aureuscctribesbackend.testutils;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import org.junit.Before;
import org.junit.Test;

public class PlayerITModelTest {

  private Player player;
  private PlayerITModel playerITModel;

  @Before
  public void setUp() {
    player = new Player();
    player.setPlayerName("test");
    player.setPassword("testPassword");
    playerITModel = new PlayerITModel(player);
  }

  @Test
  public void testIsAccountNonExpired() {
    assertFalse(playerITModel.isAccountNonExpired());
  }

  @Test
  public void testIsAccountNonLocked() {
    assertFalse(playerITModel.isAccountNonLocked());
  }

  @Test
  public void testIsCredentialsNonExpired() {
    assertFalse(playerITModel.isCredentialsNonExpired());
  }

  @Test
  public void testIsEnabled() {
    assertFalse(playerITModel.isEnabled());
  }

  @Test
  public void testGetPlayer() {
    assertEquals(player, playerITModel.getPlayer());
  }

  @Test
  public void testSetPlayer() {
    Player newPlayer = new Player();
    player.setPlayerName("test");
    player.setPassword("testPassword");
    playerITModel.setPlayer(newPlayer);
    assertEquals(newPlayer, playerITModel.getPlayer());
  }
}