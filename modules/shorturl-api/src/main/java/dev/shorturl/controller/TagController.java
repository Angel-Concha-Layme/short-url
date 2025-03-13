package dev.shorturl.controller;

import dev.shorturl.dto.TagDTO;
import dev.shorturl.security.SecurityContext;
import dev.shorturl.services.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagDTO tagDTO) {
    TagDTO createdTag = tagService.createTag(tagDTO, SecurityContext.getUserOrFail());
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
    return ResponseEntity.ok(tagService.getTagById(id));
  }

  @GetMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<List<TagDTO>> getAllTags() {
    return ResponseEntity.ok(tagService.getAllTags(SecurityContext.getUserOrFail()));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @Valid @RequestBody TagDTO tagDTO) {
    return ResponseEntity.ok(tagService.updateTag(id, tagDTO, SecurityContext.getUserOrFail()));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    tagService.deleteTag(id, SecurityContext.getUserOrFail());
    return ResponseEntity.noContent().build();
  }
}
