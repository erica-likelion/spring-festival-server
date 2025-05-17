package likelion.festival.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import likelion.festival.config.CustomUserDetails;
import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.AdminDeleteDto;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.FcmTokenRequest;
import likelion.festival.exceptions.AdminPermissionException;
import likelion.festival.exceptions.EntityNotFoundException;
import likelion.festival.exceptions.InvalidRequestException;
import likelion.festival.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final WaitingService waitingService;
    private final GuestWaitingService guestWaitingService;
    private final FCMService fcmService;
    private final WaitingRepository waitingRepository;
    private final PubService pubService;

    public List<AdminWaitingList> getWaitingList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AdminPermissionException("관리자 권한이 없습니다. 관리자 계정으로 로그인해주세요.");
        }
        String name = userDetails.getUsername();
        Pub pub = pubService.getPubByName(name);

        List<AdminWaitingList> adminWaitingList = new ArrayList<>();
        adminWaitingList.addAll(waitingService.getAdminWaitingList(pub.getId()));
        adminWaitingList.addAll(guestWaitingService.getAdminWaitingList(pub.getId()));

        adminWaitingList.sort(Comparator.comparing(AdminWaitingList::getWaitingNum));
        return adminWaitingList;
    }

    @Transactional
    public void completeWaiting(AdminDeleteDto adminDeleteDto) {
        if (adminDeleteDto.getType().equals("Online")) {
            sendCompleteWaitingMessage(deleteWaitingAndReturnFcmToken(adminDeleteDto.getId()));
        } else if (adminDeleteDto.getType().equals("WalkIn")) {
            guestWaitingService.deleteGuestWaiting(adminDeleteDto.getId());
        } else {
            throw new RuntimeException("Invalid waiting type");
        }
    }

    @Transactional
    public void deleteWaiting(AdminDeleteDto adminDeleteDto) {
        if (adminDeleteDto.getType().equals("Online")) {
            sendDeletedWaitingMessage(deleteWaitingAndReturnFcmToken(adminDeleteDto.getId()));
        } else if (adminDeleteDto.getType().equals("WalkIn")) {
            guestWaitingService.deleteGuestWaiting(adminDeleteDto.getId());
        } else {
            throw new InvalidRequestException("type 을 잘 못 전달했습니다. Online, WalkIn 중에 하나로 입력해주세요.");
        }
    }

    @Transactional
    public String deleteWaitingAndReturnFcmToken(Long waitingId) {
        Waiting waiting = waitingService.getWaiting(waitingId);
        User user = waiting.getUser();
        waitingRepository.delete(waiting);
        return user.getFcmToken();
    }

    private void sendCompleteWaitingMessage(String token) {
        fcmService.sendFcmMessage(token,
                "한양대 에리카 봄 축제",
                "주점에 입장하셨습니다. 재밌게 즐겨주세요!");
    }

    private void sendDeletedWaitingMessage(String token) {
        fcmService.sendFcmMessage(token,
                "한양대 에리카 봄 축제",
                "주점에 노쇼하셔서 웨이팅이 취소되었습니다.");
    }
}
