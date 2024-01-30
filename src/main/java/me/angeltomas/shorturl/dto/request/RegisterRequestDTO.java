package me.angeltomas.shorturl.dto.request;

public record RegisterRequestDTO(
        String name,
        String email,
        String password
) {
}
