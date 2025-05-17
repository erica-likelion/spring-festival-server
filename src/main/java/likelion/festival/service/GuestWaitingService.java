package likelion.festival.service;

import likelion.festival.config.CustomUserDetails;
import likelion.festival.domain.GuestWaiting;
import likelion.festival.domain.Pub;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.GuestWaitingRequestDto;
import likelion.festival.exceptions.AdminPermissionException;
import likelion.festival.exceptions.EntityNotFoundException;
import likelion.festival.repository.GuestWaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestWaitingService {
    private final GuestWaitingRepository guestWaitingRepository;
    private final PubService pubService;

    @Transactional
    public GuestWaiting addGuestWaiting(GuestWaitingRequestDto guestWaitingRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AdminPermissionException("관리자 계정만 현장 예약을 추가할 수 있습니다.");
        }

        String name = userDetails.getUsername();

        Pub pub = pubService.getPubByName(name);

        return guestWaitingRepository.save(
                new GuestWaiting(guestWaitingRequestDto.getVisitorCount(),
                        guestWaitingRequestDto.getPhoneNumber(),
                        pub.addWaitingNum(),
                        pub)
        );
    }

    public List<AdminWaitingList> getAdminWaitingList(Long pubId) {
        return guestWaitingRepository.findGuestWaitingListByPubId(pubId);
    }

    @Transactional
    public void deleteGuestWaiting(Long guestWaitingId) {
        GuestWaiting guestWaiting = guestWaitingRepository.findById(guestWaitingId)
                .orElseThrow(() -> new EntityNotFoundException("WalkIn Waiting is not found given id " + guestWaitingId));
        guestWaitingRepository.delete(guestWaiting);
    }
}
