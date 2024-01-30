package me.angeltomas.shorturl.persistence.repository;

import me.angeltomas.shorturl.persistence.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
}