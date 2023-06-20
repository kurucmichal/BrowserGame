package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.models.Troop;

import com.greenfoxacademy.aureuscctribesbackend.repositories.TroopRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TroopServiceIntegrationTest {

  @InjectMocks
  private TroopServiceImpl troopService;

  @Mock
  private TroopRepository troopRepository;

  @Test(expected = NoSuchElementException.class)
  public void testGetNonexistentTroopById() {
    troopService.getTroopById(999L);
  }

  @Test
  public void whenValidId_thenTroopShouldBeFound() {
    Long id = 1L;
    Troop troop = new Troop();
    troop.setId(id);
    troop.setType("Test Troop");

    when(troopRepository.findById(id)).thenReturn(Optional.of(troop));

    Troop found = troopService.getTroopById(id);

    assertEquals(found.getId(), id);
  }

}
