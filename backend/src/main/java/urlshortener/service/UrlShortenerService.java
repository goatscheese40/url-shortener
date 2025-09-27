package urlshortener.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import urlshortener.model.Url;
import urlshortener.repository.UrlRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UrlShortenerService {

    private  UrlRepository urlRepository;

    public UrlShortenerService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url createShortUrl(String fullUrl, String customAlias) {
        validateInput(fullUrl,customAlias);
        if (customAlias == null) {
            customAlias =  UUID.randomUUID().toString().substring(0, 6);
        }
        Url url = new Url(customAlias, fullUrl);
        urlRepository.save(url);
        return url;
    }

    public Optional<String> getFullUrl(String customAlias) {
        return urlRepository.findById(customAlias).map(Url::getFullUrl);
    }

    public boolean deleteShortenedUrl(String customAlias) {
        if (urlRepository.existsById(customAlias)) {
            urlRepository.deleteById(customAlias);
            return true;
        }
        return false;
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    private void validateInput(String fullUrl, String customAlias) {
        final Pattern URL_REGEX = Pattern.compile("https?://.+");
        final Pattern ALIAS_REGEX = Pattern.compile("^[a-zA-Z0-9_-]{1,50}$");

        if (fullUrl == null || !URL_REGEX.matcher(fullUrl).matches()) {
            throw new IllegalArgumentException("Invalid input for full URL");
        }
        if (customAlias != null) {
            if (!ALIAS_REGEX.matcher(customAlias).matches()) {
                throw new IllegalArgumentException("Invalid input for custom alias");
            }
            boolean aliasExists = urlRepository.existsById(customAlias);
            if (aliasExists) {
                throw new IllegalArgumentException("Custom alias already exists");
            }
        }
    }
}
