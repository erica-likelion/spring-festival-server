package likelion.festival.waiting.service;

import likelion.festival.exceptions.global.EntityNotFoundException;
import likelion.festival.pub.service.PubService;
import likelion.festival.waiting.domain.GuestWaiting;
import likelion.festival.pub.domain.Pub;
import likelion.festival.user.domain.User;
import likelion.festival.waiting.dto.AdminWaitingList;
import likelion.festival.waiting.dto.GuestWaitingRequestDto;
import likelion.festival.waiting.repository.GuestWaitingRepository;
import lombok.RequiredArgsConstructor;
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
    public GuestWaiting addGuestWaiting(GuestWaitingRequestDto guestWaitingRequestDto, User user) {
        Pub pub = pubService.getPubByName(user.getEmail());

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
        pubService.updateEnterNum(guestWaiting.getWaitingNum(), guestWaiting.getPub().getId());
        guestWaitingRepository.delete(guestWaiting);
    }
}
