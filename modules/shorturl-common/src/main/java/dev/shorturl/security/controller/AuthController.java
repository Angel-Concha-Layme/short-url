package dev.shorturl.security.controller;

import dev.shorturl.security.dto.AuthenticationRequestDTO;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.dto.EmailVerificationRequestDTO;
import dev.shorturl.security.dto.RegisterRequestDTO;
import dev.shorturl.security.service.AuthService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authservice;

  public AuthController(AuthService authservice) {
    this.authservice = authservice;
  }

  @PostMapping("/register")
  @PermitAll
  public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
    return ResponseEntity.ok(this.authservice.register(registerRequestDTO));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
    return ResponseEntity.ok(this.authservice.authenticate(authenticationRequestDTO));
  }

  @PostMapping("/verify")
  @PermitAll
  public ResponseEntity<String> verifyEmail(@RequestBody EmailVerificationRequestDTO verificationDTO) {
    this.authservice.verifyEmail(verificationDTO);
    return ResponseEntity.ok("Email verified successfully. You can now login.");
  }
}
