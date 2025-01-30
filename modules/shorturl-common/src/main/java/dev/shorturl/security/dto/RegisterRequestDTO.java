package dev.shorturl.security.dto;

import dev.shorturl.security.model.Role;

public record RegisterRequestDTO(
    String firstName,
    String lastName,
    String email,
    String password,
    Role role
) {
}
