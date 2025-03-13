package dev.shorturl.repository;

import dev.shorturl.model.AppUser;
import dev.shorturl.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
  List<Link> findByAppUser(AppUser user);
}
