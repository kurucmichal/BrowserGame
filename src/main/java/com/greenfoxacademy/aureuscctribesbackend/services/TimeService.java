package com.greenfoxacademy.aureuscctribesbackend.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class TimeService {

  public long minutesBetweenTimeStamps(LocalDateTime later, LocalDateTime earlier) {

    long diffMinutes = ChronoUnit.MINUTES.between(later, earlier);

    return Math.abs(diffMinutes);
  }

  public long minutesBetweenStampAndNow(LocalDateTime earlier) {

    LocalDateTime now = LocalDateTime.now();
    long diffMinutes = ChronoUnit.MINUTES.between(now, earlier);

    return Math.abs(diffMinutes);
  }

  public LocalDateTime calculateTroopCompletion(long requiredLevel) {
    return LocalDateTime.now().plusSeconds(requiredLevel * 30);
  }

  public LocalDateTime calculateBuildingCompletion(long requiredLevel) {
    return LocalDateTime.now().plusSeconds(requiredLevel * 60);
  }

  public LocalDateTime calculateTimeCompletion(String inputClass, long requiredLevel) {

    switch (inputClass) {
      case "Troop":
        return calculateTroopCompletion(requiredLevel);
      case "Farm":
        return calculateBuildingCompletion(requiredLevel);
      case "Mine":
        return calculateBuildingCompletion(requiredLevel);
      case "TownHall":
        return calculateBuildingCompletion(requiredLevel);
      case "Academy":
        return calculateBuildingCompletion(requiredLevel);
      default:
        throw new NoSuchElementException("No such kingdom attribute exists " + inputClass);
    }
  }
}
