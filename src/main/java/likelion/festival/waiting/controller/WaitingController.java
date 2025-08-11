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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waitings")
public class WaitingController {
    private final WaitingService waitingService;

    @PostMapping
    public WaitingResponseDto makeWaiting(@RequestBody WaitingRequestDto waitingRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        return waitingService.addWaiting(user, waitingRequestDto);
    }

    @DeleteMapping
    public String removeWaiting(@RequestParam Long id) {
        waitingService.deleteWaiting(id);
        return "Delete Successfully!";
    }

    @GetMapping
    public List<MyWaitingList> getWaitingList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        return waitingService.getWaitingList(user);
    }
}
