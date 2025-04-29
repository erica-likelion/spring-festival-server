package likelion.festival.controller;

import likelion.festival.domain.GuestWaiting;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.GuestWaitingRequestDto;
import likelion.festival.dto.GuestWaitingResponseDto;
import likelion.festival.service.GuestWaitingService;
import likelion.festival.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final WaitingService waitingService;
    private final GuestWaitingService guestWaitingService;

    @DeleteMapping("/admin/waiting")
    public ResponseEntity<String> deleteWaitingByAdmin(@RequestParam Integer waitingNum) {
        waitingService.deleteWaiting(waitingNum);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/waiting")
    public List<AdminWaitingList> getWaitingList(@RequestParam Long pubId) {
        return waitingService.getAdminWaitingList(pubId);
    }

    @PostMapping("/admin/waiting")
    public GuestWaitingResponseDto addGuestWaiting(@RequestBody GuestWaitingRequestDto guestWaitingRequestDto) {
        GuestWaiting guestWaiting = guestWaitingService.addGuestWaiting(guestWaitingRequestDto);

        return new GuestWaitingResponseDto(guestWaiting);
    }
}
