package urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urlshortener.model.Url;
import urlshortener.service.UrlShortenerService;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UrlShortenerControllerTest {

    private UrlShortenerService urlShortenerService;
    private UrlShortenerController urlShortenerController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        urlShortenerService = mock(UrlShortenerService.class);
        urlShortenerController = new UrlShortenerController(urlShortenerService);
        mockMvc = MockMvcBuilders.standaloneSetup(urlShortenerController).build();
    }

    @Test
    void checkForSuccessfulShortUrlCreation() throws Exception {
        Url url = new Url("alias1", "https://example.com");
        when(urlShortenerService.createShortUrl("https://example.com", "alias1")).thenReturn(url);

        Map<String, String> body = Map.of(
                "fullUrl", "https://example.com",
                "customAlias", "alias1"
        );

        mockMvc.perform(post("/api/url-shortener/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/alias1"));

    }

    @Test
    void checkForUnsuccessfulShortUrlCreation() throws Exception {
        when(urlShortenerService.createShortUrl("https://example.com", "alias1"))
                .thenThrow(new IllegalArgumentException("Custom alias already exists"));

        Map<String, String> body = Map.of(
                "fullUrl", "https://example.com",
                "customAlias", "alias1"
        );

        mockMvc.perform(post("/api/url-shortener/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Custom alias already exists"));
    }

    @Test
    void checkForSuccessfulRedirect() throws Exception {
        when(urlShortenerService.getFullUrl("alias1"))
                .thenReturn(Optional.of("https://example.com"));

        mockMvc.perform(get("/api/url-shortener/alias1"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.fullUrl").value( "https://example.com"));
    }

    @Test
    void checkForUnsuccessfulRedirect() throws Exception {
        when(urlShortenerService.getFullUrl("alias1"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/url-shortener/alias1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Custom alias not found"));
    }

    @Test
    void checkForSuccessDelete() throws Exception {
        when(urlShortenerService.deleteShortenedUrl("alias1"))
                .thenReturn(true);

        mockMvc.perform(delete("/api/url-shortener/alias1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void checkForUnsuccessDelete() throws Exception {
        when(urlShortenerService.deleteShortenedUrl("alias1"))
                .thenReturn(false);

        mockMvc.perform(delete("/api/url-shortener/alias1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Custom alias not found"));
    }
}
