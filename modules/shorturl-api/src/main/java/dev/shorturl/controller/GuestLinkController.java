package dev.shorturl.controller;

import dev.shorturl.dto.guestLink.GuestLinkResponseDTO;
import dev.shorturl.dto.guestLink.GuestLinkRequestDTO;
import dev.shorturl.services.GuestLinkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guest-links")
public class GuestLinkController {

  private final GuestLinkService guestLinkService;

  public GuestLinkController(GuestLinkService guestLinkService) {
    this.guestLinkService = guestLinkService;
  }

  @PostMapping
  public ResponseEntity<GuestLinkResponseDTO> createGuestLink(@Valid @RequestBody GuestLinkRequestDTO request) {
    GuestLinkResponseDTO guestLinkResponseDTO = GuestLinkResponseDTO.of(this.guestLinkService.createGuestLink(request));
    return ResponseEntity.status(HttpStatus.CREATED).body(guestLinkResponseDTO);
  }
}
