package dev.shorturl.controller;

import dev.shorturl.security.dto.ChangePasswordRequestDTO;
import dev.shorturl.security.dto.AuthenticationRequestDTO;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.dto.RegisterRequestDTO;
import dev.shorturl.services.AuthService;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @PostMapping("/change-password")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
    return ResponseEntity.ok(this.authservice.changePassword(changePasswordRequestDTO));
  }
}
