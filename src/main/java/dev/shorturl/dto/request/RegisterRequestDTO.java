package dev.shorturl.dto.request;

public record RegisterRequestDTO(
        String name,
        String email,
        String password
) {
}
