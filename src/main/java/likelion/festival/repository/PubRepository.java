package likelion.festival.repository;

import likelion.festival.domain.Pub;
import likelion.festival.dto.PubResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PubRepository extends JpaRepository<Pub, Long> {
    @Query("SELECT new likelion.festival.dto.PubResponseDto(p.id, p.name ,p.likeCount) FROM Pub p ORDER BY p.likeCount DESC")
    List<PubResponseDto> findAllByOrderByLikeCountDesc();

    @Query("SELECT new likelion.festival.dto.PubResponseDto(p.id, p.name, p.likeCount) FROM Pub p ORDER BY p.id")
    List<PubResponseDto> findAllOrderById();

    Optional<Pub> findByName(String name);

    @Query("""
    SELECT p FROM Pub p
    LEFT JOIN FETCH p.waitingList w
    LEFT JOIN FETCH p.guestWaitingList g
    WHERE p.id = :pubId
""")
    Optional<Pub> findPubWithWaitingsAndGuestWaitings(Long pubId);

    @Modifying
    @Query("UPDATE Pub p SET p.enterNum = :waitingNum WHERE p.id = :pubId")
    void incrementEnterNum(@Param("waitingNum") int waitingNum, @Param("pubId") Long pubId);
}
