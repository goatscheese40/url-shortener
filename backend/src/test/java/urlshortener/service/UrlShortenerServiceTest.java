package urlshortener.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import urlshortener.model.Url;
import urlshortener.repository.UrlRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UrlShortenerServiceTest {
    private UrlRepository urlRepository;
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    void setUp() {
        urlRepository = mock(UrlRepository.class);
        urlShortenerService = new UrlShortenerService(urlRepository);
    }

    /*Testing URL object generation*/
    @Test
    void checkShortUrlGenerated() {
        when(urlRepository.existsById(anyString())).thenReturn(false);
        when(urlRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        Url result = urlShortenerService.createShortUrl("https://example.com", "alias1");
        assertEquals("alias1", result.getCustomAlias());
        assertEquals("https://example.com", result.getFullUrl());
        verify(urlRepository).save(any());
    }

    @Test
    void checkShortUrlGeneratedIfAliasNull() {
        when(urlRepository.existsById(anyString())).thenReturn(false);
        when(urlRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Url result = urlShortenerService.createShortUrl("https://example.com", null);

        assertNotNull(result.getCustomAlias());
        assertEquals("https://example.com", result.getFullUrl());
        verify(urlRepository).save(any());
    }

    /*Testing validation logic before URL object creation*/
    @Test
    void checkShortUrlThrowsIfAliasTaken() {
        when(urlRepository.existsById("takenAlias")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> urlShortenerService.createShortUrl("https://example.com", "takenAlias"));
    }

    @Test
    void checkShortUrlThrowsIfFullUrlNull() {
        assertThrows(IllegalArgumentException.class,
                () -> urlShortenerService.createShortUrl(null, "takenAlias"));
    }

    @Test
    void checkShortUrlThrowsIfFullUrlMalformed() {
        assertThrows(IllegalArgumentException.class,
                () -> urlShortenerService.createShortUrl("//example.com", "takenAlias"));
    }

    @Test
    void checkShortUrlThrowsIfShortUrlMalformed() {
        assertThrows(IllegalArgumentException.class,
                () -> urlShortenerService.createShortUrl("https://example.com", "custom><alias"));

    }

    /*Testing Find and Delete URL methods*/
    @Test
    void checkFindUrlForGivenAliasReturnsUrl() {
        Url url = new Url("alias1", "https://example.com");
        when(urlRepository.findById("alias1")).thenReturn(Optional.of(url));

        Optional<String> result = urlShortenerService.getFullUrl("alias1");
        assertTrue(result.isPresent());
        assertEquals("https://example.com", result.get());
    }

    @Test
    void checkDeleteAliasReturnsTrueIfExists() {
        when(urlRepository.existsById("alias1")).thenReturn(true);
        boolean deleted = urlShortenerService.deleteShortenedUrl("alias1");
        assertTrue(deleted);
    }

    @Test
    void checkDeleteAliasReturnsFalseIfDoesNotExists() {
        when(urlRepository.existsById("alias1")).thenReturn(false);
        boolean deleted = urlShortenerService.deleteShortenedUrl("alias1");
        assertFalse(deleted);
    }
}

