package dev.shorturl.controller;

import dev.shorturl.dto.link.CompleteLinkDTO;
import dev.shorturl.dto.link.CompleteLinkRequestDTO;
import dev.shorturl.security.SecurityContext;
import dev.shorturl.services.LinkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/link")
public class LinkController {
  private final LinkService linkService;

  public LinkController(LinkService linkService) {
    this.linkService = linkService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<CompleteLinkDTO> createLink(@Valid @RequestBody CompleteLinkRequestDTO completeLinkRequestkDTO) {
    CompleteLinkDTO createdLink = linkService.createLink(completeLinkRequestkDTO, SecurityContext.getUserOrFail());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdLink);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<CompleteLinkDTO> getLinkById(@PathVariable Long id) {
    return ResponseEntity.ok(linkService.getLinkById(id));
  }

  @GetMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<List<CompleteLinkDTO>> getAllLinks() {
    return ResponseEntity.ok(linkService.getAllLinks(SecurityContext.getUserOrFail()));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<CompleteLinkDTO> updateLink(@PathVariable Long id, @Valid @RequestBody CompleteLinkRequestDTO completeLinkRequestDTO) {
    return ResponseEntity.ok(linkService.updateLink(id, completeLinkRequestDTO, SecurityContext.getUserOrFail()));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Void> deleteLink(@PathVariable Long id) {
    linkService.deleteLink(id, SecurityContext.getUserOrFail());
    return ResponseEntity.noContent().build();
  }
}
