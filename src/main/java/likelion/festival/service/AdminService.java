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
import likelion.festival.enums.RoleType;
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

    public List<AdminWaitingList> getWaitingList(User user) {
        Pub pub = pubService.getPubByName(user.getEmail());

        List<AdminWaitingList> adminWaitingList = new ArrayList<>();
        adminWaitingList.addAll(waitingService.getAdminWaitingList(pub.getId()));
        adminWaitingList.addAll(guestWaitingService.getAdminWaitingList(pub.getId()));

        adminWaitingList.sort(Comparator.comparing(AdminWaitingList::getWaitingNum));
        return adminWaitingList;
    }

    @Transactional
    public void completeWaiting(AdminDeleteDto adminDeleteDto) {
        if (adminDeleteDto.getType().equals("Online")) {
            String fcmToken = deleteWaitingAndReturnFcmToken(adminDeleteDto.getId());
            if (fcmToken != null) {
                sendCompleteWaitingMessage(fcmToken);
            }
        } else if (adminDeleteDto.getType().equals("WalkIn")) {
            guestWaitingService.deleteGuestWaiting(adminDeleteDto.getId());
        } else {
            throw new InvalidRequestException("type 을 잘 못 전달했습니다. Online, WalkIn 중에 하나로 입력해주세요.");
        }
    }

    @Transactional
    public void deleteWaiting(AdminDeleteDto adminDeleteDto) {
        if (adminDeleteDto.getType().equals("Online")) {
            String fcmToken = deleteWaitingAndReturnFcmToken(adminDeleteDto.getId());
            if (fcmToken != null) {
                sendDeletedWaitingMessage(fcmToken);
            }
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
        waitingService.deleteWaiting(waiting);
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
                "주점에 방문하지 않아서 웨이팅이 취소되었습니다.");
    }
}
