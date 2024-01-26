package me.angeltomas.shorturl.controller;

import lombok.AllArgsConstructor;
import me.angeltomas.shorturl.entity.Url;
import me.angeltomas.shorturl.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shorturl")
@AllArgsConstructor
public class ShortUrlController {

    private UrlService urlService;

}
