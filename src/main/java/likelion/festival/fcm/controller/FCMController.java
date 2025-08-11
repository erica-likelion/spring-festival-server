package likelion.festival.fcm.controller;

import jakarta.validation.Valid;
import likelion.festival.config.auth.CustomUserDetails;
import likelion.festival.user.domain.User;
import likelion.festival.fcm.dto.FcmTokenRequest;
import likelion.festival.waiting.dto.WaitingAlarmRequest;
import likelion.festival.fcm.service.FCMService;
import likelion.festival.user.service.UserService;
import likelion.festival.waiting.dto.WaitingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * FCM(Firebase Cloud Messaging) 관련 API 요청을 처리하는 컨트롤러입니다.
 *
 * @author 김승민
 */
@RestController
@RequiredArgsConstructor
public class FCMController {
    private final FCMService fcmService;
    private final UserService userService;

    /**
     * 사용자의 FCM 토큰을 저장합니다.
     * method: POST
     * url: /fcm/token
     * @param fcmTokenRequest FCM 토큰 정보가 담긴 DTO
     * @param userDetails 로그인 사용자 정보
     * @return 성공 메시지
     */
    @PostMapping("/fcm/token")
    public String getFcmToken(@Valid @RequestBody FcmTokenRequest fcmTokenRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        fcmService.saveUserFcmToken(user, fcmTokenRequest.getToken());
        return "Save FCM Token Successfully";
    }

    /**
     * 웨이팅 알림 메시지를 전송합니다.
     * method: POST
     * url: /waiting/alarm
     * @param waitingAlarmRequest 알림 전송에 필요한 웨이팅 ID, 타입(온라인 어플 웨이팅 or 현장 웨이팅), 주점 이름 정보
     * @return 알림 전송 성공/실패 여부 메시지
     */
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

    /**
     * 특정 아티스트의 콘서트 알림을 알림 설정한 사용자에게 전송합니다.
     * method: POST
     * url: /concert/alarm
     * @param artistName 알림을 보낼 아티스트의 이름
     * @return 성공 메시지
     */
    @PostMapping("/concert/alarm")
    public String sendConcertAlarm(@RequestParam String artistName) {
        fcmService.sendAllUserConcertAlarm(artistName);
        return "Send FCM Message Successfully";
    }

    /**
     * 사용자의 콘서트 알림 요청을 설정합니다.
     * method: POST
     * url: /concert/{artistName}/alarm
     * @param artistName 알림을 받을 아티스트의 이름
     * @param userDetails 로그인 사용자 정보
     * @return 성공 메시지
     */
    @PostMapping("/concert/{artistName}/alarm")
    public String setConcertAlarmRequest(@PathVariable String artistName, @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.setUserConcertAlarm(userDetails.getUser(), artistName);
        return "Set Concert Alarm Successfully";
    }

    /**
     * 사용자의 콘서트 알림 요청을 삭제합니다.
     * method: POST
     * url: /concert/{artistName}/alarm
     * @param artistName 알림 요청을 삭제할 아티스트의 이름
     * @param userDetails 로그인 사용자 정보
     * @return 성공 메시지
     */
    @DeleteMapping("/concert/{artistName}/alarm")
    public String deleteConcertAlarmRequest(@PathVariable String artistName, @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUserConcertAlarm(userDetails.getUser(), artistName);
        return "Delete Concert Alarm Successfully";
    }
}
