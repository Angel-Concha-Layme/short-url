package dev.shorturl.dto.request;

public record AuthenticationRequestDTO(
        String email,
        String password
) {
}
