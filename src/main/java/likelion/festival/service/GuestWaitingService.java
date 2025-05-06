package likelion.festival.service;

import likelion.festival.domain.GuestWaiting;
import likelion.festival.domain.Pub;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.GuestWaitingRequestDto;
import likelion.festival.exceptions.EntityNotFoundException;
import likelion.festival.repository.GuestWaitingRepository;
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
    public GuestWaiting addGuestWaiting(GuestWaitingRequestDto guestWaitingRequestDto) {
        Pub pub = pubService.getPubById(guestWaitingRequestDto.getPubId());

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
