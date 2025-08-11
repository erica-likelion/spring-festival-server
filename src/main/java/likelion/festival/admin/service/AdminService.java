package likelion.festival.admin.service;

import likelion.festival.pub.domain.Pub;
import likelion.festival.user.domain.User;
import likelion.festival.waiting.domain.Waiting;
import likelion.festival.admin.dto.AdminDeleteDto;
import likelion.festival.waiting.dto.AdminWaitingList;
import likelion.festival.fcm.dto.FcmTokenRequest;
import likelion.festival.exceptions.global.InvalidRequestException;
import likelion.festival.waiting.repository.WaitingRepository;
import likelion.festival.waiting.service.WaitingService;
import likelion.festival.waiting.service.GuestWaitingService;
import likelion.festival.fcm.service.FCMService;
import likelion.festival.pub.service.PubService;
import lombok.RequiredArgsConstructor;
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
