package likelion.festival.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.exceptions.InvalidRequestException;
import likelion.festival.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class FCMService {
    private final UserRepository userRepository;
    private final WaitingService waitingService;

    @Transactional
    public void saveUserFcmToken(User user, String fcmToken) {
        user.updateFcmToken(fcmToken);
        userRepository.save(user);
    }

    public User getUserByWaitingId(Long waitingId, String type) {
        if (type.equals("Online")) {
            return waitingService.getWaitingById(waitingId).getUser();
        } else {
            throw new InvalidRequestException("Invalid waiting type");
        }
    }

    public void sendAllUserConcertAlarm(String artistName) {
        List<String> fcmTokenList = userRepository.findAllFcmTokens();
        fcmTokenList.forEach(token -> {
            sendFcmMessage(token,
                    "한양대 에리카 봄 축제",
                    artistName + "의 공연이 이제 시작합니다!");
        });
    }

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
