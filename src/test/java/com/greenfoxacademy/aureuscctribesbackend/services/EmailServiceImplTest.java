package com.greenfoxacademy.aureuscctribesbackend.services;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceImplTest {

  @Mock
  private JavaMailSender mailSender;

  @InjectMocks
  private EmailServiceImpl emailService;

  @Test
  public void testSendEmail() throws Exception {
    String receiverEmail = "test@example.com";
    String emailTemplate = "Hello, world!";

    MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
    mimeMessageHelper.setTo(receiverEmail);
    mimeMessageHelper.setText(emailTemplate, true);

    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    doNothing().when(mailSender).send(mimeMessage);

    emailService.sendEmail(receiverEmail, emailTemplate);

    verify(mailSender, times(1)).createMimeMessage();
    verify(mailSender, times(1)).send(mimeMessage);
  }
}