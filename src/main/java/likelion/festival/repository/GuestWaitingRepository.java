package likelion.festival.repository;

import likelion.festival.domain.GuestWaiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestWaitingRepository extends JpaRepository<GuestWaiting, Long> {

}
