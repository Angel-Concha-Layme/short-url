package dev.shorturl.dto;

import dev.shorturl.model.Tag;
import jakarta.validation.constraints.NotBlank;

public record TagDTO(
    @NotBlank(message = "Tag name cannot be empty") String name,
    @NotBlank(message = "Tag color cannot be empty") String color) {

  public static TagDTO of(Tag tag) {
    return new TagDTO(tag.getName(), tag.getColor());
  }
}
