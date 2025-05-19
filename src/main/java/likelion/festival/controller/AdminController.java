package likelion.festival.controller;

import jakarta.validation.Valid;
import likelion.festival.config.CustomUserDetails;
import likelion.festival.domain.GuestWaiting;
import likelion.festival.domain.User;
import likelion.festival.dto.AdminDeleteDto;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.GuestWaitingRequestDto;
import likelion.festival.dto.GuestWaitingResponseDto;
import likelion.festival.enums.RoleType;
import likelion.festival.exceptions.AdminPermissionException;
import likelion.festival.service.AdminService;
import likelion.festival.service.GuestWaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/waiting")
public class AdminController {
    private final GuestWaitingService guestWaitingService;
    private final AdminService adminService;

    @DeleteMapping("/complete")
    public String completeWaiting(@RequestBody AdminDeleteDto adminDeleteDto) {
        adminService.completeWaiting(adminDeleteDto);
        return "complete Successfully!";
    }

    @DeleteMapping("/no-show")
    public String deleteWaiting(@RequestBody AdminDeleteDto adminDeleteDto) {
        adminService.deleteWaiting(adminDeleteDto);
        return "delete Successfully!";
    }

    @GetMapping
    public List<AdminWaitingList> getWaitingList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        if (user.getRole() != RoleType.ROLE_ADMIN) {
            throw new AdminPermissionException("관리자 권한이 없습니다. 관리자 계정으로 로그인해주세요.");
        }

        return adminService.getWaitingList(user);
    }

    @PostMapping
    public GuestWaitingResponseDto addGuestWaiting(@Valid @RequestBody GuestWaitingRequestDto guestWaitingRequestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        if (user.getRole() != RoleType.ROLE_ADMIN) {
            throw new AdminPermissionException("관리자 계정만 현장 예약을 추가할 수 있습니다.");
        }

        GuestWaiting guestWaiting = guestWaitingService.addGuestWaiting(guestWaitingRequestDto, user);
        return new GuestWaitingResponseDto(guestWaiting);
    }
}
