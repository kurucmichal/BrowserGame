package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.TownHall;
import com.greenfoxacademy.aureuscctribesbackend.repositories.TownHallRepository;
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
public class TownHallServiceImplTest {

  @Mock
  private TownHallRepository townHallRepository;

  @InjectMocks
  private TownHallServiceImpl townHallService;

  @Test
  public void testConvertTownHallToDto() {
    TownHall townHall = new TownHall();
    townHall.setId(1L);
    townHall.setLevel(1);
    townHall.setHp(100);
    townHall.setStartedAt(LocalDateTime.now());
    townHall.setFinishedAt(LocalDateTime.now().plusHours(1));

    BuildingDto dto = townHallService.convertBuildingToDto(townHall);

    assertEquals(townHall.getId(), dto.getId());
    assertEquals(townHall.getLevel(), dto.getLevel());
    assertEquals(townHall.getHp(), dto.getHp());
    assertEquals(townHall.getStartedAt(), dto.getStartedAt());
    assertEquals(townHall.getFinishedAt(), dto.getFinishedAt());
  }

  @Test
  public void testListTownHallDto() {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setName("Test Kingdom");

    TownHall townHall1 = new TownHall();
    townHall1.setId(1L);
    townHall1.setLevel(1);
    townHall1.setHp(100);
    townHall1.setKingdom(kingdom);

    TownHall townHall2 = new TownHall();
    townHall2.setId(2L);
    townHall2.setLevel(2);
    townHall2.setHp(200);
    townHall2.setKingdom(kingdom);

    List<TownHall> townHalls = Arrays.asList(townHall1, townHall2);

    when(townHallRepository.findAllByKingdom(kingdom)).thenReturn(Optional.of(townHalls));

    List<BuildingDto> result = townHallService.listTownHallDto(kingdom);

    assertEquals(townHalls.size(), result.size());

    for (int i = 0; i < townHalls.size(); i++) {
      TownHall townHall = townHalls.get(i);
      BuildingDto dto = result.get(i);

      assertEquals(townHall.getId(), dto.getId());
      assertEquals(townHall.getType(), dto.getType());
      assertEquals(townHall.getLevel(), dto.getLevel());
      assertEquals(townHall.getHp(), dto.getHp());
    }
  }

  @Test
  public void testFindAllByKingdomId() {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setName("Test Kingdom");

    TownHall townHall1 = new TownHall();
    townHall1.setId(1L);
    townHall1.setLevel(1);
    townHall1.setKingdom(kingdom);

    TownHall townHall2 = new TownHall();
    townHall2.setId(2L);
    townHall2.setLevel(2);
    townHall2.setKingdom(kingdom);

    List<TownHall> mines = Arrays.asList(townHall1, townHall2);

    when(townHallRepository.findAllByKingdom(kingdom)).thenReturn(Optional.of(mines));

    List<TownHall> result = townHallService.findAllByKingdomId(kingdom);
    assertEquals(mines, result);

    verify(townHallRepository).findAllByKingdom(kingdom);
  }

  @Test
  public void testSaveTownHall() {
    TownHall townHall = new TownHall();
    townHall.setLevel(4);

    when(townHallRepository.save(townHall)).thenReturn(townHall);

    TownHall savedTownHall = townHallService.save(townHall);

    assertNotNull(savedTownHall);
    assertEquals(townHall, savedTownHall);
    verify(townHallRepository, times(1)).save(townHall);
  }
}