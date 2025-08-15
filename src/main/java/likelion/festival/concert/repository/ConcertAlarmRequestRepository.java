package likelion.festival.concert.repository;

import likelion.festival.concert.domain.ConcertAlarmRequest;
import likelion.festival.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConcertAlarmRequestRepository extends JpaRepository<ConcertAlarmRequest, Long> {
    Optional<ConcertAlarmRequest> findByArtistNameAndUser(String artistName, User user);
}
