package likelion.festival.schedule;

import likelion.festival.repository.UserRepository;
import likelion.festival.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PubOpenAlarmService {

    private final UserRepository userRepository;
    private final FCMService fcmService;

    @Scheduled(cron = "0 0 17 * * *", zone = "Asia/Seoul")
    public void openAlarm() {
        List<String> fcmTokenList = userRepository.findAllFcmTokens();

        for (String token : fcmTokenList) {
            fcmService.sendFcmMessage(
                    token,
                    "한양대 에리카 봄 축제",
                    "주점들이 오픈했습니다! 저희 어플에서 메뉴판과 웨이팅까지 이용해보세요!"
            );
        }
    }
}
