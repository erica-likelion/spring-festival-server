package likelion.festival.service;

import likelion.festival.dto.AdminDeleteDto;
import likelion.festival.dto.AdminWaitingList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final WaitingService waitingService;
    private final GuestWaitingService guestWaitingService;

    public List<AdminWaitingList> getWaitingList(Long pubId) {
        List<AdminWaitingList> adminWaitingList = new ArrayList<>();
        adminWaitingList.addAll(waitingService.getAdminWaitingList(pubId));
        adminWaitingList.addAll(guestWaitingService.getAdminWaitingList(pubId));

        return adminWaitingList;
    }

    @Transactional
    public void deleteWaiting(AdminDeleteDto adminDeleteDto) {
        if (adminDeleteDto.getType().equals("Online")) {
            waitingService.deleteWaiting(adminDeleteDto.getId());
        } else if (adminDeleteDto.getType().equals("WalkIn")) {
            guestWaitingService.deleteGuestWaiting(adminDeleteDto.getId());
        } else {
            throw new RuntimeException("Invalid waiting type");
        }
    }
}
