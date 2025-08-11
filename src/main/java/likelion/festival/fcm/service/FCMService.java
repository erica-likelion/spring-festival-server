package likelion.festival.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import likelion.festival.exceptions.global.InvalidRequestException;
import likelion.festival.user.domain.User;
import likelion.festival.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * FCM(Firebase Cloud Messaging) 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 * @author 김승민
 */
@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class FCMService {
    private final UserRepository userRepository;
    private final likelion.festival.waiting.service.WaitingService waitingService;

    /**
     * 사용자의 FCM 토큰을 저장합니다.
     *
     * @param user 토큰을 저장할 사용자
     * @param fcmToken 저장할 FCM 토큰
     */
    @Transactional
    public void saveUserFcmToken(User user, String fcmToken) {
        user.updateFcmToken(fcmToken);
        userRepository.save(user);
    }

    /**
     * 웨이팅 ID와 타입을 기반으로 사용자를 조회합니다.
     *
     * @param waitingId 조회할 웨이팅의 ID
     * @param type 웨이팅의 타입 (온라인 어플 웨이팅 or 현장 웨이팅)
     * @return 웨이팅에 연결된 {@link User} 엔티티
     * @throws InvalidRequestException 현장 웨이팅인 경우 발생
     */
    public User getUserByWaitingId(Long waitingId, String type) {
        if (type.equals("Online")) {
            return waitingService.getWaitingById(waitingId).getUser();
        } else {
            throw new InvalidRequestException("Invalid waiting type");
        }
    }

    /**
     * 특정 아티스트의 콘서트 알람을 설정한 모든 사용자에게 알림을 전송합니다.
     *
     * @param artistName 알림을 보낼 아티스트의 이름
     */
    public void sendAllUserConcertAlarm(String artistName) {
        List<String> fcmTokenList = userRepository.findAllFcmTokensByArtistName(artistName);
        fcmTokenList.forEach(token -> {
            sendFcmMessage(token,
                    "한양대 에리카 봄 축제",
                    artistName + "의 공연이 이제 시작합니다!");
        });
    }

    /**
     * 주어진 FCM 토큰으로 푸시 알림 메시지를 전송합니다.
     *
     * @param token 알림을 받을 기기의 FCM 토큰
     * @param title 알림 제목
     * @param body 알림 내용
     * @throws RuntimeException FCM 메시지 전송 중 예외 발생 시
     */
    public void sendFcmMessage(String token, String title, String body){
        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(token)
                    .build());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
