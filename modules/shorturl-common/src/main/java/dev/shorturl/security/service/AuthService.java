package dev.shorturl.security.service;

import dev.shorturl.security.dto.AuthenticationRequestDTO;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.dto.RegisterRequestDTO;
import dev.shorturl.security.model.Token;
import dev.shorturl.security.model.TokenType;
import dev.shorturl.security.model.User;
import dev.shorturl.security.repository.TokenRepository;
import dev.shorturl.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

  private final UserRepository userRepository;
  private final JWTService jwtService;
  private final TokenRepository tokenRepository;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;


  public AuthService(UserRepository userRepository, JWTService jwtService, TokenRepository tokenRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.tokenRepository = tokenRepository;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
  }

  public AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO) {
    var user = new User();
    user.setFirstName(registerRequestDTO.firstName());
    user.setLastName(registerRequestDTO.lastName());
    user.setEmail(registerRequestDTO.email());
    user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
    user.setRole(registerRequestDTO.role());

    var savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    saveUserToken(savedUser, jwtToken);

    return new AuthenticationResponseDTO(jwtToken, refreshToken);
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = new Token();
    token.setUser(user);
    token.setToken(jwtToken);
    token.setTokenType(TokenType.BEARER);
    token.setExpired(false);
    token.setRevoked(false);

    tokenRepository.save(token);
  }

  public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authenticationRequestDTO.email(),
            authenticationRequestDTO.password()
        )
    );
    var user = userRepository.findByEmail(authenticationRequestDTO.email())
        .orElseThrow();

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return new AuthenticationResponseDTO(jwtToken, refreshToken);
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
