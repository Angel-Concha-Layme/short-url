package dev.shorturl.dto.guestLink;

import dev.shorturl.model.GuestLink;

import java.time.LocalDateTime;

public record GuestLinkResponseDTO(
    Long id,
    String url,
    String slug,
    LocalDateTime createdAt,
    LocalDateTime expiresAt
) {
  public static GuestLinkResponseDTO of(GuestLink guestLink) {
    return new GuestLinkResponseDTO(
        guestLink.getId(),
        guestLink.getUrl(),
        guestLink.getSlug(),
        guestLink.getCreatedAt(),
        guestLink.getExpiresAt()
    );
  }
}
