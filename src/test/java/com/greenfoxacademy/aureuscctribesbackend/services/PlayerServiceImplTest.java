package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerIdNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.PlayerNameNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceImplTest {

  @Mock
  private PlayerRepository playerRepository;

  private PlayerServiceImpl playerService;

  @Before
  public void setUp() {
    playerRepository = Mockito.mock(PlayerRepository.class);
    playerService = new PlayerServiceImpl(playerRepository);
  }

  @Test
  public void testSavePlayer() {
    String playerName = "testPlayer";
    String password = "password";
    Kingdom kingdom = new Kingdom();

    Player expectedPlayer = new Player(playerName, password, kingdom);

    when(playerRepository.save(expectedPlayer)).thenReturn(expectedPlayer);
    Player actualPlayer = playerService.savePlayer(expectedPlayer);
    assertEquals(expectedPlayer, actualPlayer);
  }

  @Test
  public void testGetPlayerByPlayerName() throws PlayerNameNotFoundException {
    String playerName = "testPlayer";
    Kingdom kingdom = new Kingdom();

    Player expectedPlayer = new Player();
    expectedPlayer.setPlayerName(playerName);
    expectedPlayer.setPassword("password");
    expectedPlayer.setKingdom(kingdom);

    when(playerRepository.findByPlayerName(playerName)).thenReturn(Optional.of(expectedPlayer));
    Player actualPlayer = playerService.getPlayerByPlayerName(playerName);

    assertNotNull(actualPlayer);
    assertEquals(expectedPlayer.getPlayerName(), actualPlayer.getPlayerName());
    assertEquals(expectedPlayer.getPassword(), actualPlayer.getPassword());
    assertEquals(expectedPlayer.getKingdom(), actualPlayer.getKingdom());
  }

  @Test(expected = PlayerNameNotFoundException.class)
  public void testGetPlayerByPlayerNameNotFound() throws PlayerNameNotFoundException {
    String playerName = "nonexistentPlayer";
    when(playerRepository.findByPlayerName(playerName)).thenReturn(Optional.empty());
    playerService.getPlayerByPlayerName(playerName);

  }

  @Test
  public void getPlayerByIdWhenPlayerExists() {
    long playerId = 1L;
    Player player = new Player();
    player.setId(playerId);
    player.setPlayerName("Test");
    when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

    Player result = playerService.getPlayerById(playerId);

    assertEquals(player, result);
  }

  @Test(expected = PlayerIdNotFoundException.class)
  public void getPlayerByIdWhenPlayerDoesNotExist() {
    long playerId = 1L;
    when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

    playerService.getPlayerById(playerId);
  }
}
