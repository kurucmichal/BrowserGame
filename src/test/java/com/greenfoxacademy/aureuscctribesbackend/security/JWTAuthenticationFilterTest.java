package com.greenfoxacademy.aureuscctribesbackend.security;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.greenfoxacademy.aureuscctribesbackend.services.LoggingServiceImpl;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@RunWith(MockitoJUnitRunner.class)
public class JWTAuthenticationFilterTest {

  @Mock
  private JwtService jwtService;

  @Mock
  private CustomUserDetailsService customUserDetailsService;

  @Mock
  private LoggingServiceImpl loggingService;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private FilterChain filterChain;

  @InjectMocks
  private JWTAuthenticationFilter jwtAuthenticationFilter;

  @Test
  public void testDoFilterInternal() throws Exception {
    String authToken = "Bearer [JWT]";
    String username = "testUser";
    UserDetails userDetails = new User(username, "", new ArrayList<>());
    when(request.getHeader("Authorization")).thenReturn(authToken);
    when(jwtService.extractUsername(anyString())).thenReturn(username);
    when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
    when(jwtService.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertEquals(userDetails.getUsername(), authentication.getName());
    assertNull(authentication.getCredentials());
    assertTrue(authentication.isAuthenticated());

    verify(filterChain).doFilter(request, response);
  }
}