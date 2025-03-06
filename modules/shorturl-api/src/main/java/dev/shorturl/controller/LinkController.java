package dev.shorturl.controller;

import dev.shorturl.services.LinkService;
import org.springframework.stereotype.Controller;

@Controller
public class LinkController {
  private final LinkService linkService;

  public LinkController(LinkService linkService) {
    this.linkService = linkService;
  }
}
