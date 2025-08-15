package likelion.festival.pub.schedule;

import likelion.festival.user.repository.UserRepository;
import likelion.festival.fcm.service.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PubOpenAlarmService {

    private final UserRepository userRepository;
    private final FCMService fcmService;

    // 테스트 용으로 매일 오후 5시 알림보냄, 실제 배포에는 삭제 요망
    @Scheduled(cron = "0 0 17 * * *", zone = "Asia/Seoul")
    public void openAlarm() {
        List<String> fcmTokenList = userRepository.findAllFcmTokens();

        for (String token : fcmTokenList) {
            fcmService.sendFcmMessage(
                    token,
                    "한양대 에리카 봄 축제",
                    "주점들이 오픈했습니다! 테스트용 주점 알림입니다"
            );
        }
    }

    @Scheduled(cron = "0 0 17 28 5 *", zone = "Asia/Seoul")
    public void openAlarm1() {
        List<String> fcmTokenList = userRepository.findAllFcmTokens();

        for (String token : fcmTokenList) {
            fcmService.sendFcmMessage(
                    token,
                    "한양대 에리카 봄 축제",
                    "주점들이 오픈했습니다! 저희 어플에서 메뉴판과 웨이팅까지 이용해보세요!"
            );
        }
    }

    @Scheduled(cron = "0 0 17 29 5 *", zone = "Asia/Seoul")
    public void openAlarm2() {
        List<String> fcmTokenList = userRepository.findAllFcmTokens();

        for (String token : fcmTokenList) {
            fcmService.sendFcmMessage(
                    token,
                    "한양대 에리카 봄 축제",
                    "주점들이 오픈했습니다! 저희 어플에서 메뉴판과 웨이팅까지 이용해보세요!"
            );
        }
    }

    @Scheduled(cron = "0 0 17 30 5 *", zone = "Asia/Seoul")
    public void openAlarm3() {
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
