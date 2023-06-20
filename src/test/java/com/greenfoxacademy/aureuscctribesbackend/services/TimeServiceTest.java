package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TimeServiceTest {

  @InjectMocks
  private TimeService timeService;

  @Test
  public void minutesBetweenTimeStampsTest() {
    LocalDateTime earlier = LocalDateTime.of(2022, 3, 7, 12, 0);
    LocalDateTime later = LocalDateTime.of(2022, 3, 7, 12, 20);
    int result = 20;
    assertEquals(result, timeService.minutesBetweenTimeStamps(later, earlier));

  }

  @Test
  public void minutesBetweenStampAndNowTest() {
    LocalDateTime now = LocalDateTime.now();
    assertEquals(0, timeService.minutesBetweenStampAndNow(now));
  }

  @Test
  public void calculateTroopCompletionTest() {

    int level = 3;
    int delta = level * 30;
    LocalDateTime now = LocalDateTime.now().plusSeconds(delta).truncatedTo(ChronoUnit.SECONDS);
    assertEquals(now, timeService.calculateTroopCompletion(level).truncatedTo(ChronoUnit.SECONDS));
  }

  @Test
  public void calculateBuildingCompletionTest() {
    int level = 3;
    int delta = level * 60;
    LocalDateTime now = LocalDateTime.now().plusSeconds(delta).truncatedTo(ChronoUnit.SECONDS);
    assertEquals(now, timeService.calculateBuildingCompletion(level).truncatedTo(ChronoUnit.SECONDS));
  }

  @Test
  public void calculateTimeCompletionTroopTest() {
    int level = 3;
    String modelName = "Troop";
    int delta = level * 30;
    LocalDateTime now = LocalDateTime.now().plusSeconds(delta).truncatedTo(ChronoUnit.SECONDS);
    assertEquals(now, timeService.calculateTimeCompletion(modelName,level).truncatedTo(ChronoUnit.SECONDS));

  }

  @Test
  public void calculateTimeCompletionBuildingTest() {
    int level = 3;
    String modelName = "Farm";
    int delta = level * 60;
    LocalDateTime now = LocalDateTime.now().plusSeconds(delta).truncatedTo(ChronoUnit.SECONDS);
    assertEquals(now, timeService.calculateTimeCompletion(modelName,level).truncatedTo(ChronoUnit.SECONDS));

  }

  @Test(expected = NoSuchElementException.class)
  public void calculateTimeCompletionInvalidTest() {
    int level = 3;
    String modelName = "SpanishInquisition";
    int delta = level * 60;
    LocalDateTime now = LocalDateTime.now().plusSeconds(delta).truncatedTo(ChronoUnit.SECONDS);
    assertEquals(now, timeService.calculateTimeCompletion(modelName,level).truncatedTo(ChronoUnit.SECONDS));

  }
}