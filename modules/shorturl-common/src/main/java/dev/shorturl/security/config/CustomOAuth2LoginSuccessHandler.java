package dev.shorturl.security.config;

import dev.shorturl.security.model.User;
import dev.shorturl.security.repository.UserRepository;
import dev.shorturl.security.service.JWTService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JWTService jwtService;
  private final UserRepository userRepository;

  public CustomOAuth2LoginSuccessHandler(JWTService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }


  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    String email = (String) oAuth2User.getAttributes().get("email");

    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new RuntimeException("User not found after OAuth login")
    );

    String jwt = jwtService.generateToken(user);

    response.setContentType("application/json");
    response.getWriter().write("{\"token\": \"" + jwt + "\"}");
    response.getWriter().flush();

  }
}
