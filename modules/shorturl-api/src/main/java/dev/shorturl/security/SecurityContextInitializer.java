package dev.shorturl.security;

import dev.shorturl.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextInitializer {

  @Autowired
  public SecurityContextInitializer(AppUserRepository appUserRepository) {
    SecurityContext.setAppUserRepository(appUserRepository);
  }
}
