package likelion.festival.repository;

import likelion.festival.domain.ConcertAlarmRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertAlarmRequestRepository extends JpaRepository<ConcertAlarmRequest, Long> {
}
