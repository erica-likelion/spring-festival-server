package likelion.festival.controller;

import likelion.festival.domain.GuestWaiting;
import likelion.festival.dto.AdminDeleteDto;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.GuestWaitingRequestDto;
import likelion.festival.dto.GuestWaitingResponseDto;
import likelion.festival.service.AdminService;
import likelion.festival.service.GuestWaitingService;
import likelion.festival.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/waiting")
public class AdminController {
    private final GuestWaitingService guestWaitingService;
    private final AdminService adminService;

    @DeleteMapping
    public String deleteWaitingByAdmin(@RequestBody AdminDeleteDto adminDeleteDto) {
        adminService.deleteWaiting(adminDeleteDto);
        return "Delete Successfully!";
    }

    @GetMapping
    public List<AdminWaitingList> getWaitingList(@RequestParam Long pubId) {
        return adminService.getWaitingList(pubId);
    }

    @PostMapping
    public GuestWaitingResponseDto addGuestWaiting(@RequestBody GuestWaitingRequestDto guestWaitingRequestDto) {
        GuestWaiting guestWaiting = guestWaitingService.addGuestWaiting(guestWaitingRequestDto);

        return new GuestWaitingResponseDto(guestWaiting);
    }
}
