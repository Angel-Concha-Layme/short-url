package dev.shorturl.security;

import dev.shorturl.model.AppUser;
import dev.shorturl.repository.AppUserRepository;
import dev.shorturl.security.model.User;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class SecurityContext {

  @Setter
  private static AppUserRepository appUserRepository;

  public static AppUser getUserOrFail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated() ||
        !(authentication.getPrincipal() instanceof User securityUser)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    if (appUserRepository == null) {
      throw new IllegalStateException("AppUserRepository has not been initialized");
    }

    return appUserRepository.findBySecurityUser(securityUser)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "AppUser not found"));
  }
}
