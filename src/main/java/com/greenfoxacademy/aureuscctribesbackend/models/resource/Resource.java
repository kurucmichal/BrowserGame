package com.greenfoxacademy.aureuscctribesbackend.models.resource;

import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.sun.istack.NotNull;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public abstract class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  protected Integer quantity;

  private boolean valid;

  @OneToOne(mappedBy = "player", fetch = FetchType.EAGER)
  protected Kingdom kingdom;

  public Resource() {
    this.quantity = 50;
  }

  public Resource(int quantity, Kingdom kingdom) {
    this.quantity = quantity;
    this.kingdom = kingdom;
    this.valid = isValid();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isValid() {
    return quantity != null && quantity >= 0;
  }


  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Kingdom getKingdom() {
    return kingdom;
  }

  public void setKingdom(Kingdom kingdom) {
    this.kingdom = kingdom;
  }

  @Override
  public String toString() {
    return "Resource{"
        + "id=" + id
        + ", quantity=" + quantity
        + ", kingdom=" + kingdom
        + '}';
  }
}
