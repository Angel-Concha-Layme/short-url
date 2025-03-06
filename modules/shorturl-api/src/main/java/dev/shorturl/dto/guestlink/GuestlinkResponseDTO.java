package dev.shorturl.dto.guestlink;

import dev.shorturl.model.GuestLink;

import java.time.LocalDateTime;

public record GuestlinkResponseDTO(
    Long id,
    String url,
    String slug,
    LocalDateTime createdAt,
    LocalDateTime expiresAt
) {
  public static GuestlinkResponseDTO of(GuestLink guestLink) {
    return new GuestlinkResponseDTO(
        guestLink.getId(),
        guestLink.getUrl(),
        guestLink.getSlug(),
        guestLink.getCreatedAt(),
        guestLink.getExpiresAt()
    );
  }
}
