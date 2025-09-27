package urlshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import urlshortener.model.Url;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, String> {

    @Override
    Optional<Url> findById(String customAlias);

    @Override
    void deleteById(String customAlias);

    @Override
    List<Url> findAll();
}
