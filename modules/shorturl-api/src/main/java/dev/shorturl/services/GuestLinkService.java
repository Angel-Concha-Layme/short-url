package dev.shorturl.services;

import dev.shorturl.dto.guestLink.GuestLinkRequestDTO;
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

  public GuestLink createGuestLink(GuestLinkRequestDTO request) {
    GuestLink guestLink = new GuestLink();
    guestLink.setUrl(request.url());
    guestLink.setSlug(this.slugService.generateSlug());
    guestLink.setCreatedAt(LocalDateTime.now());
    guestLink.setExpiresAt(LocalDateTime.now().plusDays(7));


    return this.guestLinkRepository.save(guestLink);
  }
}
