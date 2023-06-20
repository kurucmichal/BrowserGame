package com.greenfoxacademy.aureuscctribesbackend.configuration;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfiguration {

  private final String smtpHost;
  private final String smtpUsername;
  private final String smtpPassword;
  private Integer smtpPort;

  @Autowired
  public EmailConfiguration(
      @Value("${SMTP_HOST}") String smtpHost,
      @Value("${SMTP_PORT}") Integer smtpPort,
      @Value("${SMTP_USERNAME}") String smtpUsername,
      @Value("${SMTP_PASSWORD}") String smtpPassword) {
    this.smtpHost = smtpHost;
    this.smtpPort = smtpPort;
    this.smtpUsername = smtpUsername;
    this.smtpPassword = smtpPassword;
  }

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost(smtpHost);
    mailSender.setPort(smtpPort);
    mailSender.setUsername(smtpUsername);
    mailSender.setPassword(smtpPassword);
    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");
    return mailSender;
  }
}
