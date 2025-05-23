package likelion.festival.repository;

import likelion.festival.domain.ConcertAlarmRequest;
import likelion.festival.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConcertAlarmRequestRepository extends JpaRepository<ConcertAlarmRequest, Long> {
    Optional<ConcertAlarmRequest> findByArtistNameAndUser(String artistName, User user);
}
