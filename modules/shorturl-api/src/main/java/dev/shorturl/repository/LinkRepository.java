package dev.shorturl.repository;

import dev.shorturl.model.AppUser;
import dev.shorturl.model.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
  List<Link> findByAppUser(AppUser user);

  Optional<Link> findByIdAndAppUser(Long id, AppUser user);
}
