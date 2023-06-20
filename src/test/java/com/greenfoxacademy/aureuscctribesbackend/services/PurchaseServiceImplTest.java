package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.exceptions.NoBuildingFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ResourceInvalidException;
import com.greenfoxacademy.aureuscctribesbackend.models.Building;
import com.greenfoxacademy.aureuscctribesbackend.models.Farm;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.models.Troop;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import org.hibernate.procedure.NoSuchParameterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseServiceImplTest {

  @Mock
  private BuildingServiceImpl buildingService;
  @Mock
  private ResourceServiceImpl resourceService;
  @Mock
  private TroopServiceImpl troopService;
  @InjectMocks
  private PurchaseServiceImpl purchaseService;

  private Kingdom kingdom;
  private Gold gold;
  private Gold goldNotEnough;

  @Before
  public void setUp() {
    Player player = new Player("test");
    player.setPassword("tttt");
    kingdom = new Kingdom("Test kingdom");
    kingdom.setPlayer(player);
    kingdom.setId(1L);
    gold = new Gold(1000, kingdom);
    goldNotEnough = new Gold(10, kingdom);
    kingdom.setGold(gold);

  }

  @Test
  public void isAffordableIsTrue() {

    when(resourceService.getGold(any(Kingdom.class))).thenReturn(gold);
    boolean test = purchaseService.isAffordable(kingdom, 1, "troop");
    assertTrue(test);

  }

  @Test(expected = NoSuchParameterException.class)
  public void isAffordableThrowExceptionForIncorrectParameter() {

    when(resourceService.getGold(any(Kingdom.class))).thenReturn(gold);
    boolean test = purchaseService.isAffordable(kingdom, 1, "test");
  }

  @Test
  public void isAffordableIsFalseForNotSufficientResources() {

    when(resourceService.getGold(any(Kingdom.class))).thenReturn(goldNotEnough);
    boolean test = purchaseService.isAffordable(kingdom, 1, "troop");
    assertFalse(test);
  }

  @Test
  public void purchaseBuildingWithEnoughResourcesWillReturnBuilding() {

    Building expectedFarm = new Farm();
    PurchaseServiceImpl spy = spy(
        new PurchaseServiceImpl(buildingService, resourceService, troopService));

    doReturn(true).when(spy).isAffordable(any(Kingdom.class), anyInt(), any(String.class));
    doReturn(100).when(spy).getPrice(anyInt(), any());

    doNothing().when(resourceService).updateResource(any(Long.class), anyInt(), anyInt());

    when(buildingService.create(any(Kingdom.class), any(String.class))).thenReturn(expectedFarm);

    Building test = spy.purchaseBuilding(kingdom, "farm");

    assertEquals(expectedFarm, test);
    verify(resourceService, times(1)).updateResource(any(Long.class), anyInt(), anyInt());
  }

  @Test(expected = ResourceInvalidException.class)
  public void purchaseBuildingWithNotEnoughResourceThrowException() {
    PurchaseServiceImpl spy = spy(
        new PurchaseServiceImpl(buildingService, resourceService, troopService));
    doReturn(false).when(spy).isAffordable(any(Kingdom.class), anyInt(), any(String.class));
    Building test = spy.purchaseBuilding(kingdom, "farm");

  }

  @Test
  public void purchaseTroopWithEnoughResourcesWillReturnTroop() {

    Troop expectedTroop = new Troop(kingdom);
    PurchaseServiceImpl spy = spy(
        new PurchaseServiceImpl(buildingService, resourceService, troopService));
    doReturn(true).when(spy).isAffordable(any(Kingdom.class), anyInt(), any(String.class));
    when(troopService.create(any(Kingdom.class))).thenReturn(expectedTroop);
    Troop test = spy.purchaseTroop(kingdom);

    assertEquals(expectedTroop, test);
  }

  @Test(expected = ResourceInvalidException.class)
  public void purchaseTroopWithNotEnoughResourceThrowException() {
    PurchaseServiceImpl spy = spy(
        new PurchaseServiceImpl(buildingService, resourceService, troopService));
    doReturn(false).when(spy).isAffordable(any(Kingdom.class), anyInt(), any(String.class));
    Troop test = spy.purchaseTroop(kingdom);
  }

  @Test
  public void upgradeBuildingSuccessfully() throws NoBuildingFoundException {

    Building farm = new Farm();
    farm.setKingdom(kingdom);
    Building upgradedFarm = new Farm();
    upgradedFarm.setLevel(2);
    upgradedFarm.setHp(200);
    PurchaseServiceImpl spy = spy(
        new PurchaseServiceImpl(buildingService, resourceService, troopService));

    when(buildingService.getById(any(Long.class))).thenReturn(farm);

    doReturn(true).when(spy).isAffordable(any(), anyInt(), any(String.class));

    when(buildingService.upgrade(any(Building.class), anyInt())).thenReturn(upgradedFarm);

    Building test = spy.upgradeBuilding(1L);

    assertEquals(2, test.getLevel());
    assertEquals(200, test.getHp());
    verify(resourceService, times(1)).updateResource(any(Long.class), anyInt(), anyInt());

  }

  @Test(expected = NoBuildingFoundException.class)
  public void upgradeBuildingNotFoundById() throws NoBuildingFoundException {

    try {
      when(buildingService.getById(any(Long.class))).thenThrow(NoBuildingFoundException.class);
      purchaseService.upgradeBuilding(1L);

    } catch (NoBuildingFoundException e) {
      System.err.println(e.getMessage());
      throw e;
    }
  }

  @Test(expected = ResourceInvalidException.class)
  public void upgradeBuildingThrowNotEnoughResourceException() throws NoBuildingFoundException {

    PurchaseServiceImpl spy = spy(
        new PurchaseServiceImpl(buildingService, resourceService, troopService));
    try {
      when(buildingService.getById(any(Long.class))).thenReturn(new Farm());
      doReturn(false).when(spy).isAffordable(any(), anyInt(), any(String.class));
      spy.upgradeBuilding(1L);

    } catch (ResourceInvalidException e) {
      System.err.println(e.getMessage());
      throw e;
    }
  }

  @Test
  public void upgradeTroopSuccessfully() {

    Troop originalTroop = new Troop(kingdom);
    Troop upgradedTroop = new Troop(kingdom);
    upgradedTroop.setLevel(2);
    upgradedTroop.setHp(40);
    upgradedTroop.setAttack(20);
    upgradedTroop.setDefense(10);
    PurchaseServiceImpl spy = spy(
        new PurchaseServiceImpl(buildingService, resourceService, troopService));

    doReturn(true).when(spy).isAffordable(any(), anyInt(), any(String.class));
    when(troopService.upgrade(any(Troop.class), anyInt(), any())).thenReturn(upgradedTroop);

    Troop test = spy.upgradeTroop(originalTroop);

    assertEquals(upgradedTroop.getLevel(), test.getLevel());
    assertEquals(upgradedTroop.getHp(), test.getHp());
    assertEquals(upgradedTroop.getAttack(), test.getAttack());
    assertEquals(upgradedTroop.getDefense(), test.getDefense());
    verify(resourceService, times(1)).updateResource(any(Long.class), anyInt(), anyInt());

  }

  @Test(expected = ResourceInvalidException.class)
  public void troopUpgradeNotAffordableThrowException() {

    Troop troop = new Troop(kingdom);
    PurchaseServiceImpl spy = spy(
        new PurchaseServiceImpl(buildingService, resourceService, troopService));

    doReturn(false).when(spy).isAffordable(any(), anyInt(), any());
    spy.upgradeTroop(troop);
  }
}