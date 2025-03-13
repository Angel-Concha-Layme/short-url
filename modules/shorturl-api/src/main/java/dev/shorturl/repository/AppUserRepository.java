package dev.shorturl.repository;

import dev.shorturl.model.AppUser;
import dev.shorturl.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findBySecurityUser(User securityUser);
}
