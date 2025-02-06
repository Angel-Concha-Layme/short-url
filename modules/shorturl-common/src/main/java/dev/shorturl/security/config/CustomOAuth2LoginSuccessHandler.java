package dev.shorturl.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shorturl.security.dto.AuthenticationResponseDTO;
import dev.shorturl.security.model.Role;
import dev.shorturl.security.model.User;
import dev.shorturl.security.repository.UserRepository;
import dev.shorturl.security.service.JWTService;
import dev.shorturl.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ObjectMapper jacksonObjectMapper;

    public CustomOAuth2LoginSuccessHandler(JWTService jwtService, UserRepository userRepository,
                                             PasswordEncoder passwordEncoder, TokenService tokenService,
                                             ObjectMapper jacksonObjectMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth2 Attributes: " + oAuth2User.getAttributes());
        String email = (String) oAuth2User.getAttributes().get("email");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            String firstName = (String) oAuth2User.getAttributes().getOrDefault("given_name", "Username");
            String lastName = (String) oAuth2User.getAttributes().getOrDefault("family_name", "Default");
            String defaultPassword = UUID.randomUUID().toString();
            Role role = Role.USER;

            User newUser = new User();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode(defaultPassword));
            newUser.setRole(role);

            return userRepository.save(newUser);
        });

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(user, jwtToken);

        AuthenticationResponseDTO authResponse = new AuthenticationResponseDTO(jwtToken, refreshToken);
        response.setContentType("application/json");
        String jsonResponse = jacksonObjectMapper.writeValueAsString(authResponse);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}