package me.angeltomas.shorturl.service;

import lombok.RequiredArgsConstructor;
import me.angeltomas.shorturl.dto.request.AuthenticationRequestDTO;
import me.angeltomas.shorturl.dto.request.RegisterRequestDTO;
import me.angeltomas.shorturl.dto.response.AuthenticationResponseDTO;
import me.angeltomas.shorturl.persistence.entity.user.Role;
import me.angeltomas.shorturl.persistence.entity.user.User;
import me.angeltomas.shorturl.persistence.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(RegisterRequestDTO request) {
        var user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDTO(jwtToken);
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userRepository.findByEmail(request.email())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponseDTO(jwtToken);
    }
}
