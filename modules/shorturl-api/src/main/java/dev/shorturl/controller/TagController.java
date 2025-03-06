package dev.shorturl.controller;

import dev.shorturl.services.TagService;
import org.springframework.stereotype.Controller;

@Controller
public class TagController {
  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }
}
