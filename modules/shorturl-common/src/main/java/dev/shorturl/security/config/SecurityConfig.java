package dev.shorturl.security.config;

import dev.shorturl.security.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private static final String[] WHITE_LIST_URL = {
      "/",
      "/api/auth/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui/**",
      "/webjars/**",
      "/swagger-ui.html"
  };

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final LogoutHandler logoutHandler;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;
  private final org.springframework.security.authentication.AuthenticationProvider authenticationProvider;

  public SecurityConfig(
      JwtAuthenticationFilter jwtAuthFilter,
      LogoutHandler logoutHandler,
      CustomOAuth2UserService customOAuth2UserService,
      CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler,
      org.springframework.security.authentication.AuthenticationProvider authenticationProvider
  ) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.logoutHandler = logoutHandler;
    this.customOAuth2UserService = customOAuth2UserService;
    this.customOAuth2LoginSuccessHandler = customOAuth2LoginSuccessHandler;
    this.authenticationProvider = authenticationProvider;
  }

  @Bean
  @Order(1)
  public SecurityFilterChain oauthSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/oauth2/**", "/login/oauth2/**", "/api/oauth/**")
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .oauth2Login(oauth -> oauth
            .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
            .successHandler(customOAuth2LoginSuccessHandler)
        );

    return http.build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/**")
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITE_LIST_URL).permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) ->
                SecurityContextHolder.clearContext()
            )
        );
    return http.build();
  }


  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

}
