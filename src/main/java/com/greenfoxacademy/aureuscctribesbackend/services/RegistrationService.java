package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.AuthDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.LoginDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterResponseDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;

public interface RegistrationService {

  RegisterResponseDto register(RegisterDto registerDto);

  Player confirmRegistration(String token);

  AuthDto login(LoginDto loginDto);
}
