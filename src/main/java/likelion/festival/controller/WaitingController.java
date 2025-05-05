package likelion.festival.controller;

import likelion.festival.config.CustomUserDetails;
import likelion.festival.domain.User;
import likelion.festival.dto.MyWaitingList;
import likelion.festival.dto.WaitingRequestDto;
import likelion.festival.dto.WaitingResponseDto;
import likelion.festival.service.UserService;
import likelion.festival.service.WaitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waitings")
public class WaitingController {
    private final WaitingService waitingService;
    private final UserService userService;

    @PostMapping
    public WaitingResponseDto makeWaiting(@RequestBody WaitingRequestDto waitingRequestDto) {
        CustomUserDetails userDetails = getRequestUser();
        User user = userService.getUserByEmail(userDetails.getUsername());

        return waitingService.addWaiting(user, waitingRequestDto);
    }

    @DeleteMapping
    public String removeWaiting(@RequestParam Long waitingId) {
        waitingService.deleteWaiting(waitingId);
        return "Delete Successfully!";
    }

    @GetMapping
    public List<MyWaitingList> getWaitingList() {
        CustomUserDetails userDetails = getRequestUser();
        User user = userService.getUserByEmail(userDetails.getUsername());

        return waitingService.getWaitingList(user);
    }

    private CustomUserDetails getRequestUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }
}
