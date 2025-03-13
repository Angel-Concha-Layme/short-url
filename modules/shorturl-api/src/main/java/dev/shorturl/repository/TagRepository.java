package dev.shorturl.repository;

import dev.shorturl.model.AppUser;
import dev.shorturl.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByAppUser(AppUser appUser);
}
