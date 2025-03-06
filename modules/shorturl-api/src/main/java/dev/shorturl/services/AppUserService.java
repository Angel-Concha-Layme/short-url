package dev.shorturl.services;

import dev.shorturl.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {
  private final AppUserRepository appUserRepository;

  public AppUserService(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }
}
