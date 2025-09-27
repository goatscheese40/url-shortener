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
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @GetMapping(value = "/{customAlias}")
    public ResponseEntity getFullUrl(@PathVariable (required = true) String customAlias) {
        return urlShortenerService.getFullUrl(customAlias)
                .map(fullUrl -> ResponseEntity.status(302)
                .header("Location", fullUrl)
                .build())
                .orElse(ResponseEntity.status(404).body("Custom alias not found"));
    }

    @DeleteMapping(value = "/{customAlias}")
    public ResponseEntity deleteShortenedUrl(@PathVariable (required = true) String customAlias) {
        boolean deleted = urlShortenerService.deleteShortenedUrl(customAlias);
        return deleted ? ResponseEntity.status(204).build() :
                ResponseEntity.status(404).body("Custom alias not found");
    }

    @GetMapping(value = "/urls")
    public ResponseEntity getAllUrls() {
        List<Url> urls = urlShortenerService.getAllUrls();
        return ResponseEntity.status(200).body(urls);
    }
}
