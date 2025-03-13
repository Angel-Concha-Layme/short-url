package dev.shorturl.dto.link;

import dev.shorturl.dto.TagDTO;
import dev.shorturl.model.Link;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record CompleteLinkDTO(
    Long id,
    String url,
    String slug,
    String description,
    Integer clicks,
    LocalDateTime createdAt,
    LocalDateTime expiresAt,
    Set<TagDTO> tags
) {

  public static CompleteLinkDTO of(Link savedLink) {
    return new CompleteLinkDTO(
        savedLink.getId(),
        savedLink.getUrl(),
        savedLink.getSlug(),
        savedLink.getDescription(),
        savedLink.getClicks(),
        savedLink.getCreatedAt(),
        savedLink.getExpiresAt(),
        savedLink.getTags() != null
            ? savedLink.getTags().stream()
                .map(tag -> new TagDTO(tag.getName(), tag.getColor()))
                .collect(Collectors.toSet())
            : Set.of()

    );
  }
}
