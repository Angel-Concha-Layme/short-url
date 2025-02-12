package dev.shorturl.security.dto;

public record EmailVerificationRequestDTO(
        String email,
        String verificationCode
) {
}