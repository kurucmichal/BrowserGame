package com.greenfoxacademy.aureuscctribesbackend.testutils;

import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class PlayerITModel implements UserDetails {

  private Player player;

  public PlayerITModel() {
  }

  public PlayerITModel(Player player) {
    this.player = player;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return player.getPassword();
  }

  @Override
  public String getUsername() {
    return player.getPlayerName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}
