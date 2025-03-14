package dev.shorturl.security.repository;

import dev.shorturl.security.model.OAuthSessions;
import dev.shorturl.security.model.Provider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthSessionsRepository extends JpaRepository<OAuthSessions, Long> {
  Optional<OAuthSessions> findByProviderUserIdAndProvider(String providerUserId, Provider provider);
}
