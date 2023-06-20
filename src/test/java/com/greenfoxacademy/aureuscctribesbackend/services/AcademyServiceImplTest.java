package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;

import com.greenfoxacademy.aureuscctribesbackend.dtos.BuildingDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Academy;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.repositories.AcademyRepository;

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
public class AcademyServiceImplTest {

  @Mock
  private AcademyRepository academyRepository;

  @InjectMocks
  private AcademyServiceImpl academyService;

  @Test
  public void testConvertAcademyToDto() {
    Academy academy = new Academy();
    academy.setId(1L);
    academy.setLevel(1);
    academy.setHp(100);
    academy.setStartedAt(LocalDateTime.now());
    academy.setFinishedAt(LocalDateTime.now().plusHours(1));

    BuildingDto dto = academyService.convertBuildingToDto(academy);

    assertEquals(academy.getId(), dto.getId());
    assertEquals(academy.getLevel(), dto.getLevel());
    assertEquals(academy.getHp(), dto.getHp());
    assertEquals(academy.getStartedAt(), dto.getStartedAt());
    assertEquals(academy.getFinishedAt(), dto.getFinishedAt());
  }

  @Test
  public void testListAcademyDto() {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setName("Test Kingdom");

    Academy academy1 = new Academy();
    academy1.setId(1L);
    academy1.setLevel(1);
    academy1.setHp(100);
    academy1.setKingdom(kingdom);

    Academy academy2 = new Academy();
    academy2.setId(2L);
    academy2.setLevel(2);
    academy2.setHp(200);
    academy2.setKingdom(kingdom);

    List<Academy> academies = Arrays.asList(academy1, academy2);

    when(academyRepository.findAllByKingdom(kingdom)).thenReturn(Optional.of(academies));

    List<BuildingDto> result = academyService.listAcademyDto(kingdom);

    assertEquals(academies.size(), result.size());

    for (int i = 0; i < academies.size(); i++) {
      Academy academy = academies.get(i);
      BuildingDto dto = result.get(i);

      assertEquals(academy.getId(), dto.getId());
      assertEquals(academy.getType(), dto.getType());
      assertEquals(academy.getLevel(), dto.getLevel());
      assertEquals(academy.getHp(), dto.getHp());
    }
  }

  @Test
  public void testFindAllByKingdomId() {
    Kingdom kingdom = new Kingdom();
    kingdom.setId(1L);
    kingdom.setName("Test Kingdom");

    Academy academy1 = new Academy();
    academy1.setId(1L);
    academy1.setLevel(1);
    academy1.setKingdom(kingdom);

    Academy academy2 = new Academy();
    academy2.setId(2L);
    academy2.setLevel(2);
    academy2.setKingdom(kingdom);

    List<Academy> academies = Arrays.asList(academy1, academy2);

    when(academyRepository.findAllByKingdom(kingdom)).thenReturn(Optional.of(academies));

    List<Academy> result = academyService.findAllByKingdomId(kingdom);
    assertEquals(academies, result);

    verify(academyRepository).findAllByKingdom(kingdom);
  }

  @Test
  public void testSaveAcademy() {
    Academy academy = new Academy();
    academy.setLevel(4);

    when(academyRepository.save(academy)).thenReturn(academy);

    Academy savedAcademy = academyService.save(academy);

    assertNotNull(savedAcademy);
    assertEquals(academy, savedAcademy);
    verify(academyRepository, times(1)).save(academy);
  }
}