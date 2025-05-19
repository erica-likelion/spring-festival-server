package likelion.festival.repository;

import likelion.festival.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

    @Query("SELECT u.fcmToken FROM User u where u.fcmToken is not null")
    List<String> findAllFcmTokens();
}
