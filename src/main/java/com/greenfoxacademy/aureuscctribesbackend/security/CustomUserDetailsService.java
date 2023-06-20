package com.greenfoxacademy.aureuscctribesbackend.security;

import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final PlayerRepository playerRepository;

  @Autowired
  public CustomUserDetailsService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Player player = playerRepository.findByPlayerName(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    return new User(player.getPlayerName(), player.getPassword(),
        mapRolesToAuthorities(Collections.singletonList(player.getRoleType())));
  }

  private Collection<GrantedAuthority> mapRolesToAuthorities(List<RoleType> roles) {
    return roles
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
  }
}