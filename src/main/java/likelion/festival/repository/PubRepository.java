package likelion.festival.repository;

import likelion.festival.domain.Pub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PubRepository extends JpaRepository<Pub, Long> {
    List<Pub> findAllByOrderByLikeCountDesc();

    Optional<Pub> findByName(String name);
}
