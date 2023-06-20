package com.greenfoxacademy.aureuscctribesbackend.dtos;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResourcesDtoTest {

  @Test
  public void testSetResourceType() {
    ResourcesDto resources = new ResourcesDto("food", 10);
    resources.setResourceType("gold");
    assertEquals("gold", resources.getResourceType());
  }

  @Test
  public void testSetQuantity() {
    ResourcesDto resources = new ResourcesDto("food", 10);
    resources.setQuantity(20);
    assertEquals(20, resources.getQuantity());
  }
}