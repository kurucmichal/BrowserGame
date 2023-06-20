package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Mine;
import com.greenfoxacademy.aureuscctribesbackend.repositories.MineRepository;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MineServiceImplTest {

  @Mock
  private MineRepository mineRepository;

  @InjectMocks
  private MineServiceImpl mineService;

  @Test
  public void testConvertMineToDto() {
    Mine mine = new Mine();
    mine.setId(1L);
    mine.setLevel(1);
    mine.setHp(100);
    mine.setStartedAt(LocalDateTime.now());
    mine.setFinishedAt(LocalDateTime.now().plusHours(1));

    BuildingDto dto = mineService.convertBuildingToDto(mine);

    assertEquals(mine.getId(), dto.getId());
    assertEquals(mine.getLevel(), dto.getLevel());
    assertEquals(mine.getHp(), dto.getHp());
    assertEquals(mine.getStartedAt(), dto.getStartedAt());
    assertEquals(mine.getFinishedAt(), dto.getFinishedAt());
  }

  @Test
  public void testListMineDto() {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setName("Test Kingdom");

    Mine mine1 = new Mine();
    mine1.setId(1L);
    mine1.setLevel(1);
    mine1.setHp(100);
    mine1.setKingdom(kingdom);

    Mine mine2 = new Mine();
    mine2.setId(2L);
    mine2.setLevel(2);
    mine2.setHp(200);
    mine2.setKingdom(kingdom);

    List<Mine> mines = Arrays.asList(mine1, mine2);

    when(mineRepository.findAllByKingdom(kingdom)).thenReturn(Optional.of(mines));

    List<BuildingDto> result = mineService.listAllMineDto(kingdom);

    assertEquals(mines.size(), result.size());

    for (int i = 0; i < mines.size(); i++) {
      Mine mine = mines.get(i);
      BuildingDto dto = result.get(i);

      assertEquals(mine.getId(), dto.getId());
      assertEquals(mine.getType(), dto.getType());
      assertEquals(mine.getLevel(), dto.getLevel());
      assertEquals(mine.getHp(), dto.getHp());
    }
  }

  @Test
  public void testFindAllByKingdomId() {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setName("Test Kingdom");

    Mine mine1 = new Mine();
    mine1.setId(1L);
    mine1.setLevel(1);
    mine1.setKingdom(kingdom);

    Mine mine2 = new Mine();
    mine2.setId(2L);
    mine2.setLevel(2);
    mine2.setKingdom(kingdom);

    List<Mine> mines = Arrays.asList(mine1, mine2);

    when(mineRepository.findAllByKingdom(kingdom)).thenReturn(Optional.of(mines));

    List<Mine> result = mineService.findAllByKingdomId(kingdom);
    assertEquals(mines, result);

    verify(mineRepository).findAllByKingdom(kingdom);
  }

  @Test
  public void testSaveMine() {
    Mine mine = new Mine();
    mine.setLevel(4);

    when(mineRepository.save(mine)).thenReturn(mine);

    Mine savedMine = mineService.save(mine);

    assertNotNull(savedMine);
    assertEquals(mine, savedMine);
    verify(mineRepository, times(1)).save(mine);
  }
}