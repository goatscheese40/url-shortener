package urlshortener.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls")
public class Url {

    private static final String BASE_SHORT_URL = "http://localhost:8080/";

    @Id
    @Column(name = "custom_alias", length = 50, nullable = false)
    private String customAlias;

    @Column(name = "full_url", nullable = false)
    private String fullUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Url(){}

    public Url(String customAlias, String fullUrl) {
        this.customAlias = customAlias;
        this.fullUrl = fullUrl;
        this.createdAt = LocalDateTime.now();
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    @JsonIgnore
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("shortUrl")
    public  String getShortUrl() {
        return BASE_SHORT_URL + customAlias;
    }
}
