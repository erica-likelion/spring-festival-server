package likelion.festival.admin.controller;

import jakarta.validation.Valid;
import likelion.festival.config.auth.CustomUserDetails;
import likelion.festival.waiting.domain.GuestWaiting;
import likelion.festival.user.domain.User;
import likelion.festival.admin.dto.AdminDeleteDto;
import likelion.festival.waiting.dto.AdminWaitingList;
import likelion.festival.waiting.dto.GuestWaitingRequestDto;
import likelion.festival.waiting.dto.GuestWaitingResponseDto;
import likelion.festival.user.enums.RoleType;
import likelion.festival.admin.exception.AdminPermissionException;
import likelion.festival.admin.service.AdminService;
import likelion.festival.waiting.service.GuestWaitingService;
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
