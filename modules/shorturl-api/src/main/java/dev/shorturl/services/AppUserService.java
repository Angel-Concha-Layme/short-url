package dev.shorturl.services;

import dev.shorturl.model.AppUser;
import dev.shorturl.repository.AppUserRepository;
import dev.shorturl.security.dto.ChangePasswordRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppUserService {
  private final AppUserRepository appUserRepository;

  public AppUserService(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  @Transactional
  public Boolean tryChangePassword(ChangePasswordRequestDTO changePasswordRequestDTO, AppUser user) {
    if (!user.getSecurityUser().getPassword().equals(changePasswordRequestDTO.oldPassword())) {
      return false;
    }
    user.getSecurityUser().setPassword(changePasswordRequestDTO.newPassword());
    appUserRepository.save(user);
    return true;
  }
}
