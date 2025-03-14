package dev.shorturl.security.service;

import dev.shorturl.security.model.Token;
import dev.shorturl.security.model.TokenType;
import dev.shorturl.security.model.User;
import dev.shorturl.security.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final TokenRepository tokenRepository;

  public TokenService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  public void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(
        (token -> {
          token.setExpired(true);
          token.setRevoked(true);
        }));
    tokenRepository.saveAll(validUserTokens);
  }

  public void saveUserToken(User user, String jwtToken) {
    var token = new Token();
    token.setUser(user);
    token.setToken(jwtToken);
    token.setTokenType(TokenType.BEARER);
    token.setExpired(false);
    token.setRevoked(false);
    tokenRepository.save(token);
  }
}
