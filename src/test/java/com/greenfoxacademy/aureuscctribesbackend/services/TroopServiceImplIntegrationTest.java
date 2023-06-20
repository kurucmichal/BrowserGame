package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.greenfoxacademy.aureuscctribesbackend.dtos.TroopDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.repositories.TroopRepository;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TroopServiceImplIntegrationTest {

  @Autowired
  private TroopService troopService;

  @Autowired
  private TroopRepository troopRepository;

  @Autowired
  private TimeService timeService;


  @Test
  public void testCreate() {
    Kingdom kingdom = new Kingdom();
    Troop troop = troopService.create(kingdom);
    assertNotNull(troop.getId());
    assertEquals(kingdom, troop.getKingdom());
    assertEquals(1, troop.getLevel().intValue());
    assertEquals(20, troop.getHp().intValue());
    assertEquals(10, troop.getAttack().intValue());
    assertEquals(5, troop.getDefense().intValue());
    assertNotNull(troop.getStartedAt());
    assertNotNull(troop.getFinishedAt());
  }

  @Test
  public void testConvertTroopToDto() {
    Kingdom kingdom = new Kingdom();
    Troop troop = troopService.create(kingdom);

    TroopDto troopDto = troopService.convertTroopToDto(troop);

    assertThat(troopDto.getLevel()).isEqualTo(troop.getLevel());
    assertThat(troopDto.getHp()).isEqualTo(troop.getHp());
    assertThat(troopDto.getAttack()).isEqualTo(troop.getAttack());
    assertThat(troopDto.getDefense()).isEqualTo(troop.getDefense());
    assertThat(troopDto.getStartedAt()).isEqualTo(troop.getStartedAt());
    assertThat(troopDto.getFinishedAt()).isEqualTo(troop.getFinishedAt());
    assertThat(troopDto.getType()).isEqualTo(troop.getType());
  }

  @Test
  public void testUpgrade() {
    Kingdom kingdom = new Kingdom();
    Troop troop = troopService.create(kingdom);
    int newLevel = 2;
    int newHp = 40;
    int newAttack = 20;
    int newDefense = 10;

    Troop upgradedTroop = troopService.upgrade(troop, newLevel, kingdom);

    assertThat(upgradedTroop.getLevel()).isEqualTo(newLevel);
    assertThat(upgradedTroop.getHp()).isEqualTo(newHp);
  }
}


