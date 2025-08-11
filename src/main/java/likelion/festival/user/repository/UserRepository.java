package likelion.festival.user.repository;

import likelion.festival.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

    @Query("""
    SELECT u.fcmToken 
    FROM ConcertAlarmRequest c 
    JOIN c.user u 
    WHERE u.fcmToken IS NOT NULL 
    AND c.artistName = :artistName
""")
    List<String> findAllFcmTokensByArtistName(@Param("artistName") String artistName);

    @Query("""
    SELECT u.fcmToken 
    FROM User u
    WHERE u.fcmToken IS NOT NULL
""")
    List<String> findAllFcmTokens();
}
