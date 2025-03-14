package dev.shorturl.repository;

import dev.shorturl.model.AppUser;
import dev.shorturl.security.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

  Optional<AppUser> findBySecurityUser(User securityUser);
}
