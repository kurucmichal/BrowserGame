package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.NoBuildingFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ResourceInvalidException;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import org.hibernate.procedure.NoSuchParameterException;

public interface PurchaseService {

  Building purchaseBuilding(Kingdom kingdom, String buildingType) throws ResourceInvalidException;

  boolean postBuilding(Kingdom kingdom, Building building) throws ResourceInvalidException;


  Building upgradeBuilding(Long buildingId)
      throws NoBuildingFoundException, ResourceInvalidException;

  Troop purchaseTroop(Kingdom kingdom) throws ResourceInvalidException;

  boolean postTroop(Kingdom kingdom, int level) throws ResourceInvalidException;

  Troop upgradeTroop(Troop troop) throws ResourceInvalidException;

  boolean isAffordable(Kingdom kingdom, int level, String objectType)
      throws NoSuchParameterException;

  int getPrice(int level, String objectType);
}
