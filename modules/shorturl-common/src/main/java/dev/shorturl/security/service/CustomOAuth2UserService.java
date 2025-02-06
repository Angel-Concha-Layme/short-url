package dev.shorturl.security.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final RestTemplate restTemplate = new RestTemplate();

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String accessToken = userRequest.getAccessToken().getTokenValue();
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<?> entity = new HttpEntity<>(headers);
    ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
        "https://api.github.com/user/emails",
        HttpMethod.GET,
        entity,
        new ParameterizedTypeReference<>() {
        }
    );

    List<Map<String, Object>> emails = response.getBody();
    String primaryEmail = null;
    for (Map<String, Object> emailRecord : emails) {
      Boolean primary = (Boolean) emailRecord.get("primary");
      Boolean verified = (Boolean) emailRecord.get("verified");
      if (Boolean.TRUE.equals(primary) && Boolean.TRUE.equals(verified)) {
        primaryEmail = (String) emailRecord.get("email");
        break;
      }
    }

    Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
    attributes.put("email", primaryEmail);

    return new DefaultOAuth2User(oAuth2User.getAuthorities(), attributes, "id");
  }
}
