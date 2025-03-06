package dev.shorturl.services;

import dev.shorturl.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService {
  private final TagRepository tagRepository;

  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }
}
