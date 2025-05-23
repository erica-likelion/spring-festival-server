package likelion.festival.controller;

import jakarta.validation.Valid;
import likelion.festival.config.CustomUserDetails;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.FcmTokenRequest;
import likelion.festival.dto.WaitingAlarmRequest;
import likelion.festival.service.FCMService;
import likelion.festival.service.UserService;
import likelion.festival.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;
    private final UserService userService;

    @PostMapping("/fcm/token")
    public String getFcmToken(@Valid @RequestBody FcmTokenRequest fcmTokenRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        fcmService.saveUserFcmToken(user, fcmTokenRequest.getToken());
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

    @PostMapping("/concert/alarm")
    public String sendConcertAlarm(@RequestParam String artistName) {
        fcmService.sendAllUserConcertAlarm(artistName);
        return "Send FCM Message Successfully";
    }

    @PostMapping("/concert/{artistName}/alarm")
    public String setConcertAlarmRequest(@PathVariable String artistName, @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.setUserConcertAlarm(userDetails.getUser(), artistName);
        return "Set Concert Alarm Successfully";
    }

    @DeleteMapping("/concert/{artistName}/alarm")
    public String deleteConcertAlarmRequest(@PathVariable String artistName, @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUserConcertAlarm(userDetails.getUser(), artistName);
        return "Delete Concert Alarm Successfully";
    }
}
