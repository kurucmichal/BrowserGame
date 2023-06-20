package com.greenfoxacademy.aureuscctribesbackend.security;

import com.greenfoxacademy.aureuscctribesbackend.advice.RestAccessDeniedHandler;
import com.greenfoxacademy.aureuscctribesbackend.advice.RestAuthenticationEntryPoint;
import com.greenfoxacademy.aureuscctribesbackend.services.LoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CustomUserDetailsService userDetailsService;
  private final RestAccessDeniedHandler accessDeniedHandler;
  private final RestAuthenticationEntryPoint entryPoint;
  private final LoggingService loggingService;


  @Autowired
  public SecurityConfig(
      CustomUserDetailsService userDetailsService, RestAccessDeniedHandler accessDeniedHandler,
      RestAuthenticationEntryPoint entryPoint, LoggingService loggingService) {
    this.userDetailsService = userDetailsService;
    this.accessDeniedHandler = accessDeniedHandler;
    this.entryPoint = entryPoint;
    this.loggingService = loggingService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .headers().frameOptions().disable()
        .and()
        .authorizeHttpRequests()
        .antMatchers("/api/auth/**").permitAll()
        .anyRequest()
        .authenticated()
        .and().exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint(
            loggingService))
        .accessDeniedHandler(accessDeniedHandler)
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(new CustomExceptionFilter(entryPoint),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JWTAuthenticationFilter jwtAuthFilter() {
    return new JWTAuthenticationFilter();
  }
}