package me.angeltomas.shorturl.dto.request;

public record AuthenticationRequestDTO(
        String email,
        String password
) {
}
