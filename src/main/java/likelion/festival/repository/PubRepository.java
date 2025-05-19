package likelion.festival.repository;

import likelion.festival.domain.Pub;
import likelion.festival.dto.PubResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PubRepository extends JpaRepository<Pub, Long> {
    @Query("SELECT new likelion.festival.dto.PubResponseDto(p.id, p.name ,p.likeCount) FROM Pub p ORDER BY p.likeCount DESC")
    List<PubResponseDto> findAllByOrderByLikeCountDesc();

    Optional<Pub> findByName(String name);

    @Query("""
    SELECT p FROM Pub p
    LEFT JOIN FETCH p.waitingList w
    LEFT JOIN FETCH p.guestWaitingList g
    WHERE p.id = :pubId
""")
    Pub findPubWithWaitingsAndGuestWaitings(Long pubId);
}
