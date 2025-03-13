package dev.shorturl.services;

import dev.shorturl.dto.guestlink.GuestlinkRequestDTO;
import dev.shorturl.model.GuestLink;
import dev.shorturl.repository.GuestLinkRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GuestLinkService {

  private final GuestLinkRepository guestLinkRepository;
  private final SlugService slugService;

  public GuestLinkService(GuestLinkRepository guestLinkRepository, SlugService slugService) {
    this.guestLinkRepository = guestLinkRepository;
    this.slugService = slugService;
  }

  public GuestLink createGuestLink(GuestlinkRequestDTO request) {
    GuestLink guestLink = GuestLink.builder()
        .url(request.url())
        .slug(this.slugService.generateSlug())
        .createdAt(LocalDateTime.now())
        .expiresAt(LocalDateTime.now().plusDays(7))
        .build();

    return this.guestLinkRepository.save(guestLink);
  }
}
