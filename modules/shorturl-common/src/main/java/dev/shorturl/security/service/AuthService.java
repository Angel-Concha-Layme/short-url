package dev.shorturl.security.service;

import dev.shorturl.security.dto.AuthenticationRequestDTO;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.dto.EmailVerificationRequestDTO;
import dev.shorturl.security.dto.RegisterRequestDTO;
import dev.shorturl.security.exception.*;
import dev.shorturl.security.model.User;
import dev.shorturl.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final JWTService jwtService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;
  private final EmailService emailService;

  public AuthService(UserRepository userRepository, JWTService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, TokenService tokenService, EmailService emailService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
    this.emailService = emailService;
  }

  public void register(RegisterRequestDTO registerRequestDTO) {
    if (userRepository.findByEmail(registerRequestDTO.email()).isPresent()) {
      throw new EmailAlreadyExistsException("Email is already registered");
    }

    var user = new User();
    user.setFirstName(registerRequestDTO.firstName());
    user.setLastName(registerRequestDTO.lastName());
    user.setEmail(registerRequestDTO.email());
    user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
    user.setRole(registerRequestDTO.role());

    String verificationCode = generateVerificationCode();
    user.setVerificationCode(verificationCode);
    user.setEmailVerified(false);

    userRepository.save(user);

    emailService.sendVerificationEmail(user.getEmail(), verificationCode);
  }

  public AuthenticationResponseDTO verifyEmail(EmailVerificationRequestDTO verificationDTO) {
    var user = userRepository.findByEmail(verificationDTO.email())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    if (user.isEmailVerified()) {
      throw new EmailAlreadyVerifiedException("Email is already verified");
    }

    if (!user.getVerificationCode().equals(verificationDTO.verificationCode())) {
      throw new InvalidVerificationCodeException("Invalid verification code");
    }

    user.setEmailVerified(true);
    user.setVerificationCode(null);
    var savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(savedUser);
    var refreshToken = jwtService.generateRefreshToken(savedUser);

    tokenService.saveUserToken(savedUser, jwtToken);

    emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());

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
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    if (!user.isEmailVerified()) {
      throw new EmailNotVerifiedException("Email not verified");
    }

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    tokenService.revokeAllUserTokens(user);
    tokenService.saveUserToken(user, jwtToken);

    return new AuthenticationResponseDTO(jwtToken, refreshToken);
  }

  public static String generateVerificationCode() {
    return String.format("%06d", new SecureRandom().nextInt(1_000_000));
  }
}
