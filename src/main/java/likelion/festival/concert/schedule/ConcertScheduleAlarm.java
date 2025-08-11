package likelion.festival.concert.schedule;

import likelion.festival.concert.domain.Concert;
import likelion.festival.concert.repository.ConcertRepository;
import likelion.festival.user.repository.UserRepository;
import likelion.festival.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConcertScheduleAlarm {
    private final ConcertRepository concertRepository;
    private final FCMService fcmService;
    private final UserRepository userRepository;

    @Scheduled(fixedRate = 60000)
    public void notifyConcertUsers() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        Optional<Concert> concertOpt = concertRepository.findByStartTime(now);

        if (concertOpt.isPresent()) {
            Concert concert = concertOpt.get();
            String artistName = concert.getArtistName();
            List<String> fcmTokens = userRepository.findAllFcmTokensByArtistName(artistName);
            for (String token : fcmTokens) {
                fcmService.sendFcmMessage(
                        token,
                        artistName + " 공연이 시작됩니다!",
                        "지금 바로 확인하세요!"
                );
            }
        }
    }
}

