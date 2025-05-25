package likelion.festival.repository;

import likelion.festival.domain.LikeRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRequestLogRepository extends JpaRepository<LikeRequestLog, Long> {
    Optional<LikeRequestLog> findByIp(String ip);
    Boolean existsByIp(String ip);
}
