package likelion.festival.controller;

import jakarta.validation.Valid;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.FcmTokenRequest;
import likelion.festival.dto.WaitingAlarmRequest;
import likelion.festival.service.FCMService;
import likelion.festival.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;
    private final WaitingService waitingService;

    @PostMapping("{userId}/fcm/token")
    public String getFcmToken(@PathVariable Long userId, @Valid @RequestBody FcmTokenRequest fcmTokenRequest) {
        fcmService.saveUserFcmToken(userId, fcmTokenRequest.getToken());
        return "Save FCM Token Successfully";
    }

    @PostMapping("/waiting/alarm")
    public String sendWaitingAlert(@Valid @RequestBody WaitingAlarmRequest waitingAlarmRequest) {
        User user = fcmService.getUserByWaitingId(waitingAlarmRequest.getWaitingId(), waitingAlarmRequest.getType());

        fcmService.sendFcmMessage(user.getFcmToken(),
                "한양대 에리카 봄 축제",
                waitingAlarmRequest.getPubName() + "의 웨이팅이 얼마 남지 않았습니다. 근처에서 기다려주세요!");
        return "Send FCM Message Successfully";
    }
}
