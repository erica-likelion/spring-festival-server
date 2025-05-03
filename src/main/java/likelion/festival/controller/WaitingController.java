package likelion.festival.controller;

import likelion.festival.config.CustomUserDetails;
import likelion.festival.domain.User;
import likelion.festival.dto.MyWaitingList;
import likelion.festival.dto.WaitingRequestDto;
import likelion.festival.dto.WaitingResponseDto;
import likelion.festival.service.UserService;
import likelion.festival.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WaitingController {
    private final WaitingService waitingService;
    private final UserService userService;

    @PostMapping("/api/waitings")
    public WaitingResponseDto makeWaiting(@RequestBody WaitingRequestDto waitingRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());

        return waitingService.addWaiting(user, waitingRequestDto);
    }

    @DeleteMapping("/api/waitings")
    public String removeWaiting(@RequestParam Integer waitingNum) {
        waitingService.deleteWaiting(waitingNum);
        return "Delete Successfully!";
    }

    @GetMapping("/api/waitings")
    public List<MyWaitingList> getWaitingList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());

        return waitingService.getWaitingList(user);
    }
}
