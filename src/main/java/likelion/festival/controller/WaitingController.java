package likelion.festival.controller;

import likelion.festival.config.CustomUserDetails;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.WaitingRequestDto;
import likelion.festival.dto.WaitingResponseDto;
import likelion.festival.service.UserService;
import likelion.festival.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
