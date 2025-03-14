package dev.shorturl.repository;

import dev.shorturl.model.AppUser;
import dev.shorturl.model.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
  List<Tag> findByAppUser(AppUser appUser);
}
