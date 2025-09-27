package urlshortener.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlEntityTest {

    @Test
    public void testForCorrectShortUrl() {
        Url url = new Url("alias1", "https://example.com");
        assertEquals("http://localhost:8080/alias1", url.getShortUrl());
    }

}
