package com.greenfoxacademy.aureuscctribesbackend.controllers;

import com.greenfoxacademy.aureuscctribesbackend.dtos.AuthDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.LoginDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterResponseDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import com.greenfoxacademy.aureuscctribesbackend.services.RegistrationService;
import io.jsonwebtoken.SignatureException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final LoggingService loggingService;
  private final RegistrationService registrationService;


  @Autowired
  public AuthController(LoggingService loggingService,
      RegistrationService registrationService) {
    this.loggingService = loggingService;
    this.registrationService = registrationService;
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody RegisterDto registerDto, HttpServletRequest request) {
    RegisterResponseDto registerResponseDto = registrationService.register(registerDto);
    loggingService.logInfo(HttpStatus.OK.value(),
        "Player " + registerDto.getUsername() + " successfully registered", request);
    return ResponseEntity.ok(registerResponseDto);
  }

  @GetMapping("/register")
  public ResponseEntity<String> confirmRegistration(@RequestParam String token,
      HttpServletRequest request) {
    Player player = registrationService.confirmRegistration(token);
    loggingService.logInfo(HttpStatus.OK.value(),
        "Player " + player.getPlayerName() + " has been verified", request);
    return new ResponseEntity<>(
        "Your account has been successfully verified " + player.getPlayerName()
            + ". You can now log in.",
        HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
    AuthDto authDto;
    try {
      authDto = registrationService.login(loginDto);
    } catch (SignatureException e) {
      loggingService.logError(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password",
          request);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    } catch (BadCredentialsException e) {
      loggingService.logError(HttpStatus.UNAUTHORIZED.value(), "Account has not been verified",
          request);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    loggingService.logInfo(HttpStatus.OK.value(),
        "Player " + loginDto.getUsername() + " successfully logged in", request);
    return ResponseEntity.ok(authDto);
  }
}
