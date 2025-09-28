CREATE TABLE urls (
    custom_alias VARCHAR(50) PRIMARY KEY,
    full_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_full_url ON urls(full_url);