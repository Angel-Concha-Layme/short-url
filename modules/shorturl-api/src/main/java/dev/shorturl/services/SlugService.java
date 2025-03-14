package dev.shorturl.services;

import dev.shorturl.repository.GuestLinkRepository;
import java.util.Random;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SlugService {
  private final long rangeStart;
  private final long rangeEnd;
  private final GuestLinkRepository guestLinkRepository;

  public SlugService(
      @Value("${slug.range.start}") long rangeStart,
      @Value("${slug.range.end}") long rangeEnd,
      GuestLinkRepository guestLinkRepository) {
    this.rangeStart = rangeStart;
    this.rangeEnd = rangeEnd;
    this.guestLinkRepository = guestLinkRepository;
  }

  public String generateSlug() {
    String slug;
    do {
      long randomNumber = rangeStart + new Random().nextLong(rangeEnd - rangeStart);
      slug = toBase62(randomNumber);
    } while (guestLinkRepository.existsBySlug(slug));
    return slug;
  }

  private String toBase62(long number) {
    final String base62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder slug = new StringBuilder();
    while (number > 0) {
      slug.append(base62.charAt((int) (number % 62)));
      number /= 62;
    }
    return slug.reverse().toString();
  }
}
