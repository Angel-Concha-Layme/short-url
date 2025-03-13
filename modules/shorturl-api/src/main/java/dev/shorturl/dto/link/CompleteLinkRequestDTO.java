package dev.shorturl.dto.link;

import java.util.Set;

public record CompleteLinkRequestDTO(
    String url,
    String description,
    Set<Long> tagsIds
) {
}
