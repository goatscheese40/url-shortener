package urlshortener.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import urlshortener.dto.UrlRequest;
import urlshortener.model.Url;
import urlshortener.service.UrlShortenerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/url-shortener")
public class UrlShortenerController {
    UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping(value = "/shorten")
    public ResponseEntity createShortUrl(@RequestBody UrlRequest request) {
        try {
            String fullUrl = request.getFullUrl();
            String customAlias = request.getCustomAlias();
            Url url = urlShortenerService.createShortUrl(fullUrl, customAlias);

            Map<String, String> response = Map.of("shortUrl", url.getShortUrl());
            return ResponseEntity.status(201).body(response);

        } catch (IllegalArgumentException exception) {
            Map<String, String> errorResponse = Map.of("error", exception.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping(value = "/{customAlias}")
    public ResponseEntity getFullUrl(@PathVariable (required = true) String customAlias) {
        return urlShortenerService.getFullUrl(customAlias)
                .map(fullUrl -> {
                    Map<String, String> response = Map.of("fullUrl", fullUrl);
                    return ResponseEntity.status(302).body(response);
                })
                .orElseGet(() -> {
                    Map<String, String> errorResponse = Map.of("error", "Custom alias not found");
                    return ResponseEntity.status(404).body(errorResponse);
                });
    }

    @DeleteMapping(value = "/{customAlias}")
    public ResponseEntity deleteShortenedUrl(@PathVariable (required = true) String customAlias) {
        boolean deleted = urlShortenerService.deleteShortenedUrl(customAlias);
        if (deleted) {
            return ResponseEntity.status(204).build();
        } else {
            Map<String, String> errorResponse = Map.of("error", "Custom alias not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @GetMapping(value = "/urls")
    public ResponseEntity getAllUrls() {
        List<Url> urls = urlShortenerService.getAllUrls();
        return ResponseEntity.status(200).body(urls);
    }
}
