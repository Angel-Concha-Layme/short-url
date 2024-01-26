package me.angeltomas.shorturl.service;

import lombok.AllArgsConstructor;
import me.angeltomas.shorturl.entity.Url;
import me.angeltomas.shorturl.repository.UrlRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UrlService {
    private UrlRepository urlRepository;

}
