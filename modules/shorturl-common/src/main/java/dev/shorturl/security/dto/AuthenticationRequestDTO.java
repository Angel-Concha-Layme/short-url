package dev.shorturl.security.dto;

public record AuthenticationRequestDTO(
        String email,
        String password
) {
}
