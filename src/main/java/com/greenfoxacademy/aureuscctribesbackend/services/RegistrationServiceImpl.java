package com.greenfoxacademy.aureuscctribesbackend.services;

import com.greenfoxacademy.aureuscctribesbackend.dtos.AuthDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.LoginDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterDto;
import com.greenfoxacademy.aureuscctribesbackend.dtos.RegisterResponseDto;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.ObjectNotFoundException;
import com.greenfoxacademy.aureuscctribesbackend.exceptions.UsernameAlreadyUsedException;
import com.greenfoxacademy.aureuscctribesbackend.models.ConfirmationToken;
import com.greenfoxacademy.aureuscctribesbackend.models.Kingdom;
import com.greenfoxacademy.aureuscctribesbackend.models.Player;
import com.greenfoxacademy.aureuscctribesbackend.repositories.PlayerRepository;
import com.greenfoxacademy.aureuscctribesbackend.security.JwtService;
import com.greenfoxacademy.aureuscctribesbackend.security.RoleType;
import io.jsonwebtoken.SignatureException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

  private final PlayerRepository playerRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final KingdomService kingdomService;
  private final EmailService emailService;
  private final String emailVerificationLink;
  private final Boolean verifyEmail;
  private final Boolean sendEmail;
  private final PlayerService playerService;
  private final ConfirmationTokenService confirmationTokenService;

  @Autowired
  public RegistrationServiceImpl(PlayerRepository playerRepository, PasswordEncoder passwordEncoder,
      JwtService jwtService, AuthenticationManager authenticationManager,
      KingdomService kingdomService,
      EmailService emailService,
      @Value("${EMAIL_VERIFICATION_SWITCH}") Boolean verifyEmail,
      @Value("${EMAIL_VERIFICATION_LINK}") String emailVerificationLink,
      @Value("${EMAIL_SEND_SWITCH}") Boolean sendEmail,
      PlayerService playerService, ConfirmationTokenService confirmationTokenService) {
    this.playerRepository = playerRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
    this.kingdomService = kingdomService;
    this.emailService = emailService;
    this.emailVerificationLink = emailVerificationLink;
    this.verifyEmail = verifyEmail;
    this.sendEmail = sendEmail;
    this.playerService = playerService;
    this.confirmationTokenService = confirmationTokenService;
  }

  @Override
  public RegisterResponseDto register(RegisterDto registerDto) {

    if (playerService.usernameExists(registerDto.getUsername())) {
      throw new UsernameAlreadyUsedException(
          "Username: '" + registerDto.getUsername() + "' already used!");
    }

    if (playerService.emailExists(registerDto.getEmail())) {
      throw new UsernameAlreadyUsedException(
          "Email: '" + registerDto.getEmail() + "' already used!");
    }

    if (kingdomService.kingdomExist(registerDto.getKingdomName())) {
      throw new UsernameAlreadyUsedException(
          "Kingdom name: '" + registerDto.getKingdomName() + "' already used!");
    }

    Player player = new Player();
    player.setPlayerName(registerDto.getUsername());
    player.setPassword(passwordEncoder.encode(registerDto.getPassword()));
    player.setEmail(registerDto.getEmail());
    player.setRoleType(RoleType.USER);
    player.setVerified(false);
    playerRepository.save(player);

    Kingdom kingdom = kingdomService.createKingdom(registerDto.getKingdomName(), player);
    player.setKingdom(kingdom);
    playerRepository.save(player);

    ConfirmationToken confirmationToken = new ConfirmationToken();
    confirmationToken.setToken(UUID.randomUUID().toString());
    confirmationToken.setPlayer(player);
    confirmationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
    confirmationTokenService.saveConfirmationToken(confirmationToken);

    String token = confirmationToken.getToken();
    String confirmationLink = emailVerificationLink + token;

    if (!verifyEmail) {
      confirmRegistration(confirmationToken.getToken());
    }

    if (sendEmail) {
      emailService.sendEmail(player.getEmail(),
          buildEmail(player.getPlayerName(), confirmationLink));
    }

    return new RegisterResponseDto(token);
  }

  @Override
  public Player confirmRegistration(String token) {
    ConfirmationToken confirmationToken = confirmationTokenService.getConfirmationToken(token);
    if (confirmationToken == null) {
      throw new ObjectNotFoundException("Invalid token");
    }

    Player player = confirmationToken.getPlayer();
    player.verifyUser();
    playerService.addPlayer(player);

    confirmationTokenService.deleteConfirmationToken(confirmationToken);

    return player;
  }

  @Override
  public AuthDto login(LoginDto loginDto) {
    Player player = playerRepository.findByPlayerName(loginDto.getUsername())
        .orElseThrow(() -> new SignatureException("Invalid username or password"));

    if (!player.isVerified()) {
      throw new BadCredentialsException("Account has not been verified");
    }

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

    String jwtToken = jwtService.generateToken(player);
    return AuthDto.builder().token(jwtToken).build();
  }

  private String buildEmail(String username, String confirmationLink) {
    return
        "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n\n"
            + "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n\n"
            + "  <table role=\"presentation\" width=\"100%\""
            + " style=\"border-collapse:collapse;min-width:100%;width:100%!important\""
            + " cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
            + "    <tbody><tr>\n"
            + "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n\n"
            + "        <table role=\"presentation\" width=\"100%\""
            + " style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\""
            + " cellspacing=\"0\" border=\"0\" align=\"center\">\n"
            + "          <tbody><tr>\n"
            + "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
            + "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\""
            + " border=\"0\" style=\"border-collapse:collapse\">\n"
            + "                  <tbody><tr>\n"
            + "<td style=\"padding-left:10px\">\n\n"
            + "                    </td>\n"
            + "<td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n"
            + "                      <span"
            + " style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm"
            + " your email</span>\n"
            + "</td>\n"
            + "</tr>\n"
            + "</tbody></table>\n"
            + "</a>\n"
            + "</td>\n"
            + "</tr>\n"
            + "</tbody></table>\n\n"
            + "</td>\n"
            + "</tr>\n"
            + "  </tbody></table>\n"
            + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\""
            + " align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\""
            + " style=\"border-collapse:collapse;max-width:580px;width:100%!important\""
            + " width=\"100%\">\n"
            + "    <tbody><tr>\n"
            + "<td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
            + "      <td>\n\n"
            + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\""
            + " border=\"0\" style=\"border-collapse:collapse\">\n"
            + "                  <tbody><tr>\n"
            + "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
            + "                  </tr>\n"
            + "</tbody></table>\n\n"
            + "</td>\n"
            + "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n"
            + "    </tr>\n"
            + "  </tbody></table>\n\n\n\n"
            + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\""
            + " align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\""
            + " style=\"border-collapse:collapse;max-width:580px;width:100%!important\""
            + " width=\"100%\">\n"
            + "    <tbody><tr>\n"
            + "      <td height=\"30\"><br></td>\n"
            + "    </tr>\n"
            + "    <tr>\n"
            + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
            + "      <td"
            + " style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n\n"
            + "<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi "
            + username
            + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank"
            + " you for registering. Please click on the below confirmationLink to activate your"
            + " account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid"
            + " #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0"
            + " 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\""
            + confirmationLink
            + "\">Activate Now</a> </p></blockquote>\n"
            + " Link will expire in 15 minutes. <p>See you soon</p>\n</td>\n"
            + "      <td width=\"10\" valign=\"middle\"><br></td>\n"
            + "    </tr>\n<tr>\n<td height=\"30\"><br></td>\n</tr>\n"
            + "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n\n"
            + "</div></div>";
  }
}
