package dev.shorturl.services;

import dev.shorturl.repository.LinkRepository;
import org.springframework.stereotype.Service;

@Service
public class LinkService {
  private final LinkRepository linkReposityory;

  public LinkService(LinkRepository linkReposityory) {
    this.linkReposityory = linkReposityory;
  }
}
