package likelion.festival.repository;

import likelion.festival.domain.Pub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Modifying
    @Query("UPDATE Pub p SET p.enterNum = :waitingNum WHERE p.id = :pubId")
    void incrementEnterNum(@Param("waitingNum") int waitingNum, @Param("pubId") Long pubId);
}
