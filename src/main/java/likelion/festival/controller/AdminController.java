package likelion.festival.controller;

import jakarta.validation.Valid;
import likelion.festival.config.CustomUserDetails;
import likelion.festival.domain.GuestWaiting;
import likelion.festival.dto.AdminDeleteDto;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.GuestWaitingRequestDto;
import likelion.festival.dto.GuestWaitingResponseDto;
import likelion.festival.exceptions.AdminPermissionException;
import likelion.festival.service.AdminService;
import likelion.festival.service.GuestWaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public List<AdminWaitingList> getWaitingList() {
        CustomUserDetails userDetails = getRequestUser();
        validateAdminUser(userDetails);
        String pubName = userDetails.getUsername();
        return adminService.getWaitingList(pubName);
    }

    @PostMapping
    public GuestWaitingResponseDto addGuestWaiting(@Valid @RequestBody GuestWaitingRequestDto guestWaitingRequestDto) {
        CustomUserDetails userDetails = getRequestUser();
        validateAdminUser(userDetails);
        String pubName = userDetails.getUsername();
        GuestWaiting guestWaiting = guestWaitingService.addGuestWaiting(guestWaitingRequestDto, pubName);

        return new GuestWaitingResponseDto(guestWaiting);
    }

    private CustomUserDetails getRequestUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }

    private void validateAdminUser(CustomUserDetails userDetails) {
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AdminPermissionException("관리자 권한이 없습니다. 관리자 계정으로 로그인해주세요.");
        }
    }
}
