package dev.shorturl.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shorturl.helpers.NameUtils;
import dev.shorturl.model.AppUser;
import dev.shorturl.repository.AppUserRepository;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.model.OAuthSessions;
import dev.shorturl.security.model.Provider;
import dev.shorturl.security.model.Role;
import dev.shorturl.security.model.User;
import dev.shorturl.security.repository.OAuthSessionsRepository;
import dev.shorturl.security.repository.UserRepository;
import dev.shorturl.security.service.JWTService;
import dev.shorturl.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component("customOAuth2LoginSuccessHandler")
public class CustomOAuth2LoginSuccessHandlerService implements AuthenticationSuccessHandler {

  private final JWTService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final ObjectMapper jacksonObjectMapper;
  private final AppUserRepository appUserRepository;
  private final OAuthSessionsRepository oAuthSessionsRepository;
  private final OAuth2AuthorizedClientService authorizedClientService;

  public CustomOAuth2LoginSuccessHandlerService(JWTService jwtService, UserRepository userRepository,
                                                PasswordEncoder passwordEncoder, TokenService tokenService,
                                                ObjectMapper jacksonObjectMapper, AppUserRepository appUserRepository, OAuthSessionsRepository oAuthSessionsRepository, OAuth2AuthorizedClientService authorizedClientService) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
    this.jacksonObjectMapper = jacksonObjectMapper;
    this.appUserRepository = appUserRepository;
    this.oAuthSessionsRepository = oAuthSessionsRepository;
    this.authorizedClientService = authorizedClientService;
  }

  @Transactional
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    System.out.println("OAuth2 Attributes: " + oAuth2User.getAttributes());
    String email = (String) oAuth2User.getAttributes().get("email");

    User user = userRepository.findByEmail(email).orElseGet(() -> {

      String fullName = (String) oAuth2User.getAttributes().getOrDefault("name", "Username");

      String[] splitName = NameUtils.splitFullName(fullName);
      String firstName = splitName[0];
      String lastName = splitName[1];

      String defaultPassword = UUID.randomUUID().toString();
      Role role = Role.USER;

      User newUser = new User();
      newUser.setFirstName(firstName);
      newUser.setLastName(lastName);
      newUser.setEmail(email);
      newUser.setPassword(passwordEncoder.encode(defaultPassword));
      newUser.setRole(role);

      AppUser appUser = new AppUser();
      appUser.setSecurityUser(newUser);
      appUserRepository.save(appUser);

      return userRepository.save(newUser);
    });

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    tokenService.revokeAllUserTokens(user);
    tokenService.saveUserToken(user, jwtToken);


    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
    String providerId = oauthToken.getAuthorizedClientRegistrationId();
    System.out.println("Provider ID: " + providerId);
    Provider provider = Provider.valueOf(providerId.toUpperCase());
    System.out.println("Provider: " + provider);

    OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
        oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getName());

    String accessToken = (client != null && client.getAccessToken() != null)
        ? client.getAccessToken().getTokenValue() : null;

    Instant expiresAt = (client != null && client.getAccessToken() != null)
        ? client.getAccessToken().getExpiresAt()
        : null;

    String providerUserId = String.valueOf(oAuth2User.getAttributes().get("id"));

    Optional<OAuthSessions> existingSessionOpt = oAuthSessionsRepository.findByProviderUserIdAndProvider(providerUserId, provider);
    OAuthSessions session;

    if (existingSessionOpt.isPresent()) {
      session = existingSessionOpt.get();
    } else {
      session = new OAuthSessions();
      session.setUser(user);
      session.setProviderUserId(providerUserId);
      session.setProvider(provider);
    }

    session.setProviderUserId(providerUserId);
    session.setAccessToken(accessToken);
    session.setExpiresAt(expiresAt);

    oAuthSessionsRepository.save(session);


    AuthenticationResponseDTO authResponse = new AuthenticationResponseDTO(jwtToken, refreshToken);
    response.setContentType("application/json");
    String jsonResponse = jacksonObjectMapper.writeValueAsString(authResponse);
    response.getWriter().write(jsonResponse);
    response.getWriter().flush();
  }
}