package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.dtos.ResourcesDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.NullArgumentException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ResourceInvalidException;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Food;
import com.greenfoxacademy.aureuscctribesbackend.models.resource.Gold;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.FoodRepository;
import com.greenfoxacademy.aureuscctribesbackend.repositories.resourcerepository.GoldRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServiceImplTest {

  @Mock
  private GoldRepository goldRepository;
  @Mock
  private FoodRepository foodRepository;
  @InjectMocks
  private ResourceServiceImpl resourceService;
  private Kingdom kingdom;
  private Gold gold;
  private Food food;

  @Before
  public void setUp() {
    kingdom = new Kingdom();
    gold = new Gold();
    gold.setQuantity(1);
    food = new Food();
    food.setQuantity(1);
  }

  @Test
  public void saveGold() {
    when(goldRepository.save(any())).thenReturn(gold);
    assertEquals(gold, resourceService.saveGold(gold));
    verify(goldRepository).save(gold);
  }

  @Test
  public void saveFood() {
    when(foodRepository.save(any())).thenReturn(food);
    assertEquals(food, resourceService.saveFood(food));
    verify(foodRepository).save(food);
  }

  @Test(expected = ResourceInvalidException.class)
  public void saveInValidGold() {
    gold.setQuantity(-1);
    assertEquals(gold, resourceService.saveGold(gold));
    verify(goldRepository).save(gold);
  }

  @Test(expected = ResourceInvalidException.class)
  public void saveInValidFood() {
    food.setQuantity(-1);
    assertEquals(food, resourceService.saveFood(food));
    verify(foodRepository).save(food);
  }


  @Test
  public void getGoldByKingdom() {
    when(goldRepository.findByKingdom(any())).thenReturn(Optional.of(gold));
    assertEquals(gold, resourceService.getGold(kingdom));
  }

  @Test(expected = NoSuchElementException.class)
  public void testGetGoldByKingdomNotFound() {
    when(goldRepository.findByKingdom(any())).thenReturn(Optional.empty());
    resourceService.getGold(kingdom);
  }

  @Test
  public void getFoodByKingdom() {
    when(foodRepository.findByKingdom(any())).thenReturn(Optional.of(food));
    assertEquals(food, resourceService.getFood(kingdom));
  }

  @Test(expected = NoSuchElementException.class)
  public void testGetFoodByKingdomNotFound() {
    when(foodRepository.findByKingdom(any())).thenReturn(Optional.empty());
    resourceService.getFood(kingdom);
  }

  @Test
  public void testUpdateResource() {
    long kingdomId = 123L;
    int goldChange = 10;
    int foodChange = 5;

    Gold gold = new Gold();
    gold.setQuantity(20);
    when(goldRepository.findByKingdomId(kingdomId)).thenReturn(gold);

    Food food = new Food();
    food.setQuantity(30);
    when(foodRepository.findByKingdomId(kingdomId)).thenReturn(food);

    resourceService.updateResource(kingdomId, goldChange, foodChange);

    assertEquals(10, gold.getQuantity());
    verify(goldRepository).save(gold);

    assertEquals(25, food.getQuantity());
    verify(foodRepository).save(food);

  }

  @Test(expected = NullArgumentException.class)
  public void testUpdateResourceWithNullResource() {
    Long kingdomId = 123L;
    int goldChange = 10;
    int foodChange = 5;

    // food is not mocking because the updateResource method stop after exception

    when(goldRepository.findByKingdomId(kingdomId)).thenReturn(null);

    resourceService.updateResource(kingdomId, goldChange, foodChange);

    Gold gold = new Gold();
    gold.setQuantity(20);
    when(goldRepository.findByKingdomId(kingdomId)).thenReturn(gold);
    when(foodRepository.findByKingdomId(kingdomId)).thenReturn(null);

    resourceService.updateResource(kingdomId, goldChange, foodChange);
  }

  @Test
  public void testGetResourcesDtoFromKingdom() {
    Kingdom kingdom = new Kingdom();
    Food food = new Food(10, kingdom);
    Gold gold = new Gold(20, kingdom);
    kingdom.setFood(food);
    kingdom.setGold(gold);

    List<ResourcesDto> resources = resourceService.getResourcesDtoFromKingdom(kingdom);

    assertEquals(2, resources.size());

    ResourcesDto foodResource = resources.get(0);
    assertEquals("food", foodResource.getResourceType());
    assertEquals(10, foodResource.getQuantity());

    ResourcesDto goldResource = resources.get(1);
    assertEquals("gold", goldResource.getResourceType());
    assertEquals(20, goldResource.getQuantity());
  }
}
