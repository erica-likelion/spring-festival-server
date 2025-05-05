package likelion.festival.service;

import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.MyWaitingList;
import likelion.festival.dto.WaitingRequestDto;
import likelion.festival.dto.WaitingResponseDto;
import likelion.festival.exceptions.EntityNotFoundException;
import likelion.festival.exceptions.PubException;
import likelion.festival.exceptions.WaitingException;
import likelion.festival.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final PubService pubService;

    @Transactional
    public WaitingResponseDto addWaiting(User user, WaitingRequestDto waitingRequestDto){
        List<Waiting> waitingList = user.getWaitingList();
        if (waitingList != null && waitingList.size() == 3) {
            throw new WaitingException("Waiting list is full");
        }

        Pub pub = pubService.getPubById(waitingRequestDto.getPubId());
        if (waitingList != null) {
            validateDuplicatedWaiting(waitingList, pub);
        }

        Waiting waiting = save(waitingRequestDto, pub.addWaitingNum(), user, pub);
        return new WaitingResponseDto(waiting.getId(), waiting.getWaitingNum(),
                pub.getMaxWaitingNum() - pub.getEnterNum());
    }

    @Transactional
    public Waiting save(WaitingRequestDto waitingRequestDto, Integer waitingNum, User user, Pub pub) {
        return waitingRepository.save(new Waiting(waitingRequestDto.getVisitorCount(),
                waitingRequestDto.getPhoneNumber(),
                waitingRequestDto.getPubName(),
                waitingNum,
                user,
                pub));
    }

    @Transactional
    public void deleteWaiting(Long waitingId) {
        if (!waitingRepository.existsById(waitingId)) {
            throw new EntityNotFoundException("해당 id를 가진 웨이팅이 존재하지 않습니다: " + waitingId);
        }
        waitingRepository.deleteById(waitingId);
    }

    public List<MyWaitingList> getWaitingList(User user) {
        return waitingRepository.findWaitingListByUserId(user.getId());
    }

    public List<AdminWaitingList> getAdminWaitingList(Long pubId) {
        return waitingRepository.findWaitingsByPubId(pubId);
    }

    public Waiting getWaitingById(Long waitingId) {
        return waitingRepository.findById(waitingId).orElseThrow(()
                -> new WaitingException("Waiting not found"));
    }

    private void validateDuplicatedWaiting(List<Waiting> waitingList, Pub pub) {
        if (waitingList.stream()
                .anyMatch(waiting -> waiting.getPub().equals(pub))) {
            throw new PubException("이미 해당 주점에 대한 대기열이 존재합니다.");
        }
    }
}
