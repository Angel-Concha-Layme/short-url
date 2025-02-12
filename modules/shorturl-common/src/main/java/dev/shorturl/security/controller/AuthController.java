package dev.shorturl.security.controller;

import dev.shorturl.security.dto.AuthenticationRequestDTO;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.dto.EmailVerificationRequestDTO;
import dev.shorturl.security.dto.RegisterRequestDTO;
import dev.shorturl.security.service.AuthService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authservice;

  public AuthController(AuthService authservice) {
    this.authservice = authservice;
  }

  @PostMapping("/register")
  @PermitAll
  public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
    this.authservice.register(registerRequestDTO);
    return ResponseEntity.ok("Your account has been successfully registered. Please check your email to verify your account before logging in.");
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
    return ResponseEntity.ok(this.authservice.authenticate(authenticationRequestDTO));
  }

  @GetMapping("/verify")
  @PermitAll
  public ResponseEntity<AuthenticationResponseDTO> verifyEmail(@RequestBody EmailVerificationRequestDTO verificationDTO) {
    return ResponseEntity.ok(this.authservice.verifyEmail(verificationDTO));
  }
}
