package com.greenfoxacademy.aureuscctribesbackend.models;

import javax.persistence.Embeddable;

@Embeddable
public class Location {

  private int x;
  private int y;

  // constructors

  public Location(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Location() {

  }

  // getters and setters

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
