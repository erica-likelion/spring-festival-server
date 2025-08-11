package likelion.festival.waiting.controller;

import likelion.festival.config.auth.CustomUserDetails;
import likelion.festival.user.domain.User;
import likelion.festival.waiting.dto.MyWaitingList;
import likelion.festival.waiting.dto.WaitingRequestDto;
import likelion.festival.waiting.dto.WaitingResponseDto;
import likelion.festival.waiting.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 웨이팅 관련 api 요청을 받는 컨트롤러
 *   @author 김승민
 *   @see WaitingService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waitings")
public class WaitingController {
    private final WaitingService waitingService;

    /**
     * 새로운 웨이팅을 생성합니다.
     * method: POST
     * url: /api/waitings
     * @param waitingRequestDto: 웨이팅 생성에 필요한 데이터가 담긴 DTO
     * @param userDetails 로그인 사용자의 상세 정보
     * @return 생성된 웨이팅의 정보가 담긴 {@link WaitingResponseDto}
     */
    @PostMapping
    public WaitingResponseDto makeWaiting(@RequestBody WaitingRequestDto waitingRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        return waitingService.addWaiting(user, waitingRequestDto);
    }

    /**
     * 특정 웨이팅을 삭제합니다.
     * method: DELETE
     * url: /api/waitings
     * @param id: 삭제할 웨이팅의 고유 ID
     * @return 삭제 성공 메시지
     */
    @DeleteMapping
    public String removeWaiting(@RequestParam Long id) {
        waitingService.deleteWaiting(id);
        return "Delete Successfully!";
    }

    /**
     * 현재 사용자의 모든 웨이팅을 조회합니다.
     * method: GET
     * url: /api/waitings
     * @param userDetails 로그인 사용자의 상세 정보
     * @return 현재 사용자가 등록한 웨이팅 정보를 담은 {@link MyWaitingList}의 리스트
     */
    @GetMapping
    public List<MyWaitingList> getWaitingList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        return waitingService.getWaitingList(user);
    }
}
