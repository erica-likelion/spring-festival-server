package likelion.festival.repository;

import likelion.festival.domain.Pub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PubRepository extends JpaRepository<Pub, Long> {
    List<Pub> findAllByOrderByLikeCountDesc();
}
