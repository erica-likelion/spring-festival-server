package likelion.festival.controller;

import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.WaitingRequestDto;
import likelion.festival.dto.WaitingResponseDto;
import likelion.festival.service.UserService;
import likelion.festival.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        User user = userService.getUserById(waitingRequestDto.getUserId());

        return waitingService.addWaiting(user, waitingRequestDto);
    }
}
