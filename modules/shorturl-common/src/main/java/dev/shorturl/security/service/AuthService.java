package dev.shorturl.security.service;

import dev.shorturl.security.dto.AuthenticationRequestDTO;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.dto.RegisterRequestDTO;
import dev.shorturl.security.model.User;
import dev.shorturl.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

  private final UserRepository userRepository;
  private final JWTService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  public AuthService(UserRepository userRepository, JWTService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, TokenService tokenService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
  }

  public AuthenticationResponseDTO register(RegisterRequestDTO registerRequestDTO) {
    var user = new User();
    user.setFirstName(registerRequestDTO.firstName());
    user.setLastName(registerRequestDTO.lastName());
    user.setEmail(registerRequestDTO.email());
    user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
    user.setRole(registerRequestDTO.role());

    var savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(savedUser);
    var refreshToken = jwtService.generateRefreshToken(savedUser);

    tokenService.saveUserToken(savedUser, jwtToken);

    return new AuthenticationResponseDTO(jwtToken, refreshToken);
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
    tokenService.revokeAllUserTokens(user);
    tokenService.saveUserToken(user, jwtToken);

    return new AuthenticationResponseDTO(jwtToken, refreshToken);
  }
}
