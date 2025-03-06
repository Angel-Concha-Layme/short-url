package dev.shorturl.security.dto;

public record ChangePasswordRequestDTO(
    String email,
    String oldPassword,
    String newPassword
) {
}
