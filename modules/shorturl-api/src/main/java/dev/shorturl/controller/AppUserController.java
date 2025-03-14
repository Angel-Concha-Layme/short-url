package dev.shorturl.controller;

import dev.shorturl.security.SecurityContext;
import dev.shorturl.security.dto.ChangePasswordRequestDTO;
import dev.shorturl.services.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app-user")
public class AppUserController {
  private final AppUserService appUserService;

  public AppUserController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @PostMapping
  @RequestMapping("/change-password")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Boolean> changePassword(
      @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) {
    return ResponseEntity.ok(
        appUserService.tryChangePassword(
            changePasswordRequestDTO, SecurityContext.getUserOrFail()));
  }
}
