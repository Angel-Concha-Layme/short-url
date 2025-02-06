package dev.shorturl.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.model.Role;
import dev.shorturl.security.model.Token;
import dev.shorturl.security.model.TokenType;
import dev.shorturl.security.model.User;
import dev.shorturl.security.repository.TokenRepository;
import dev.shorturl.security.repository.UserRepository;
import dev.shorturl.security.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JWTService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenRepository tokenRepository;
  private final ObjectMapper jacksonObjectMapper;

  public CustomOAuth2LoginSuccessHandler(JWTService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, ObjectMapper jacksonObjectMapper) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenRepository = tokenRepository;
    this.jacksonObjectMapper = jacksonObjectMapper;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    System.out.println("OAuth2 Attributes: " + oAuth2User.getAttributes());
    String email = (String) oAuth2User.getAttributes().get("email");


    User user = userRepository.findByEmail(email).orElseGet(() -> {
      String firstName = (String) oAuth2User.getAttributes().getOrDefault("given_name", "Username");
      String lastName = (String) oAuth2User.getAttributes().getOrDefault("family_name", "Default");
      String defaultPassword = UUID.randomUUID().toString();
      Role role = Role.USER;

      User newUser = new User();
      newUser.setFirstName(firstName);
      newUser.setLastName(lastName);
      newUser.setEmail(email);
      newUser.setPassword(passwordEncoder.encode(defaultPassword));
      newUser.setRole(role);

      return userRepository.save(newUser);
    });


    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    revokeAllUserTokens(user);


    var token = new Token();
    token.setUser(user);
    token.setToken(jwtToken);
    token.setTokenType(TokenType.BEARER);
    token.setExpired(false);
    token.setRevoked(false);
    tokenRepository.save(token);

    AuthenticationResponseDTO authResponse = new AuthenticationResponseDTO(jwtToken, refreshToken);

    response.setContentType("application/json");
    String jsonResponse = jacksonObjectMapper.writeValueAsString(authResponse);
    response.getWriter().write(jsonResponse);
    response.getWriter().flush();

  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }
}
