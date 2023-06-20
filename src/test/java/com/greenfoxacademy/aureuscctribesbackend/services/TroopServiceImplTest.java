package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.repositories.TroopRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TroopServiceImplTest {

  @Mock
  private TroopRepository troopRepository;

  @InjectMocks
  private TroopServiceImpl troopService;

  @Test
  public void testSaveValidTroop() {
    Troop troop = new Troop();
    troop.setValid(true);
    troopService.saveTroop(troop);
    Mockito.verify(troopRepository, Mockito.times(1)).save(troop);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testSaveInvalidTroop() {
    Troop troop = new Troop();
    troop.setValid(false);
    doThrow(new IllegalArgumentException("Invalid troop for kingdom"))
        .when(troopRepository).save(troop);
    troopService.saveTroop(troop);
  }

  @Test
  public void testGetTroopById() {
    Troop troop = new Troop();
    when(troopRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(troop));
    assertEquals(troop, troopService.getTroopById(1L));
  }

  @Test(expected = NoSuchElementException.class)
  public void testGetTroopByIdNotFound() {
    when(troopRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
    troopService.getTroopById(1L);
  }

  @Test
  public void convertTroopToDto() {
    Troop troop = new Troop();
    troop.setId(1L);
    troop.setLevel(2);
    troop.setHp(100);
    troop.setAttack(50);
    troop.setDefense(30);

    TroopDto troopDto = troopService.convertTroopToDto(troop);

    assertNotNull(troopDto);
    assertEquals(troop.getAttack().longValue(), troopDto.getAttack());
  }

  @Test
  public void listAllTroopsDto() {
    Kingdom kingdom = new Kingdom();
    Troop troop1 = new Troop();
    troop1.setId(1L);
    troop1.setLevel(2);
    troop1.setHp(100);
    troop1.setAttack(50);
    troop1.setDefense(30);
    troop1.setStartedAt(LocalDateTime.now());
    troop1.setFinishedAt(LocalDateTime.now().plusMinutes(30));

    Troop troop2 = new Troop();
    troop2.setId(2L);
    troop2.setLevel(3);
    troop2.setHp(150);
    troop2.setAttack(70);
    troop2.setDefense(50);
    troop2.setStartedAt(LocalDateTime.now());
    troop2.setFinishedAt(LocalDateTime.now().plusMinutes(45));

    List<Troop> troops = new ArrayList<>();
    troops.add(troop1);
    troops.add(troop2);

    kingdom.setTroops(troops);

    List<TroopDto> dtos = troopService.listAllTroopsDto(kingdom);

    assertNotNull(dtos);
    assertEquals(2, dtos.size());

    TroopDto troopDto1 = dtos.get(0);
    assertNotNull(troopDto1);
    assertEquals(troop1.getDefense().longValue(), troopDto1.getDefense());

    TroopDto troopDto2 = dtos.get(1);
    assertEquals(troop2.getHp().longValue(), troopDto2.getHp());
  }
}
