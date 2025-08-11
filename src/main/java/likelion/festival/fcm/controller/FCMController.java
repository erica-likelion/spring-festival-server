package likelion.festival.fcm.controller;

import jakarta.validation.Valid;
import likelion.festival.config.auth.CustomUserDetails;
import likelion.festival.user.domain.User;
import likelion.festival.fcm.dto.FcmTokenRequest;
import likelion.festival.waiting.dto.WaitingAlarmRequest;
import likelion.festival.fcm.service.FCMService;
import likelion.festival.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> sendWaitingAlert(@Valid @RequestBody WaitingAlarmRequest waitingAlarmRequest) {
        User user = fcmService.getUserByWaitingId(waitingAlarmRequest.getWaitingId(), waitingAlarmRequest.getType());

        if (user.getFcmToken() != null) {
            fcmService.sendFcmMessage(user.getFcmToken(),
                    "한양대 에리카 봄 축제",
                    waitingAlarmRequest.getPubName() + "의 웨이팅이 얼마 남지 않았습니다. 근처에서 기다려주세요!");
            return ResponseEntity.ok("Send FCM Message Successfully");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유저의 FCM 토큰이 없습니다.");
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
