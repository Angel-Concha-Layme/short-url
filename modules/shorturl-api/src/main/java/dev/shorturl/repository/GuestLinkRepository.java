package dev.shorturl.repository;

import dev.shorturl.model.GuestLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestLinkRepository extends JpaRepository<GuestLink, Long> {
  boolean existsBySlug(String slug);
}
