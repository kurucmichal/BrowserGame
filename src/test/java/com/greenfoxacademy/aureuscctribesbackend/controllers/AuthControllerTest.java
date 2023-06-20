package com.greenfoxacademy.aureuscctribesbackend.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.aureuscctribesbackend.dtos.AuthDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.LoginDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterResponseDto;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

import com.greenfoxacademy.aureuscctribesbackend.services.RegistrationService;
import io.jsonwebtoken.SignatureException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {


  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private LoggingService loggingService;

  @MockBean
  private RegistrationService registrationService;

  @Test
  public void testRegister() throws Exception {
    RegisterDto registerDto = new RegisterDto();
    registerDto.setUsername("testUser");
    registerDto.setEmail("testEmail@test.com");
    registerDto.setPassword("testPassword");
    registerDto.setKingdomName("testKingdom");

    RegisterResponseDto registerResponseDto = new RegisterResponseDto();
    registerResponseDto.setToken("testToken");

    Mockito.when(registrationService.register(Mockito.any(RegisterDto.class)))
        .thenReturn(registerResponseDto);

    mockMvc.perform(post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .with(user("testUser").roles("USER"))
            .content(new ObjectMapper().writeValueAsString(registerDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token", is(registerResponseDto.getToken())));
  }

  @Test
  public void testLogin() throws Exception {
    LoginDto loginDto = new LoginDto();
    loginDto.setUsername("testUser");
    loginDto.setPassword("testPassword");

    AuthDto authDto = new AuthDto();
    authDto.setToken("testToken");

    Mockito.when(registrationService.login(Mockito.any(LoginDto.class))).thenReturn(authDto);

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .with(user("testUser").roles("USER"))
            .content(new ObjectMapper().writeValueAsString(loginDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token", is(authDto.getToken())));
  }

  @Test
  public void testLoginInvalid() throws Exception {
    LoginDto loginDto = new LoginDto();
    loginDto.setUsername("testUser");
    loginDto.setPassword("testPassword");

    when(registrationService.login(Mockito.any(LoginDto.class)))
        .thenThrow(new SignatureException("Invalid username or password"));

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .with(user("testUser").roles("USER"))
            .content(new ObjectMapper().writeValueAsString(loginDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testLoginNotVerified() throws Exception {
    LoginDto loginDto = new LoginDto();
    loginDto.setUsername("testUser");
    loginDto.setPassword("testPassword");

    Mockito.when(registrationService.login(Mockito.any(LoginDto.class)))
        .thenThrow(new BadCredentialsException("Account has not been verified"));

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .with(user("testUser").roles("USER"))
            .content(new ObjectMapper().writeValueAsString(loginDto)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testConfirmRegistration() throws Exception {
    String token = "testToken";
    Player player = new Player();
    player.setPlayerName("testUser");

    Mockito.when(registrationService.confirmRegistration(token)).thenReturn(player);

    mockMvc.perform(get("/api/auth/register")
            .param("token", token)
            .with(user("testUser").roles("USER")))
        .andExpect(status().isOk())
        .andExpect(content().string(
            "Your account has been successfully verified testUser. You can now log in."));
  }
}