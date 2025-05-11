package likelion.festival.repository;

import likelion.festival.domain.Pub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PubRepository extends JpaRepository<Pub, Long> {
    List<Pub> findAllByOrderByLikeCountDesc();

    Optional<Pub> findByName(String name);

    @Query("""
    SELECT p FROM Pub p
    LEFT JOIN FETCH p.waitingList w
    LEFT JOIN FETCH p.guestWaitingList g
    WHERE p.id = :pubId
""")
    Pub findPubWithWaitingsAndGuestWaitings(Long pubId);
}
