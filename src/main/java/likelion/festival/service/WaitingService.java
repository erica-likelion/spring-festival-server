package likelion.festival.service;

import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.AdminWaitingList;
import likelion.festival.dto.MyWaitingList;
import likelion.festival.dto.WaitingRequestDto;
import likelion.festival.dto.WaitingResponseDto;
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
        if (waitingList.size() > 3) {
            throw new WaitingException("Waiting list is full");
        }

        Pub pub = pubService.getPubById(waitingRequestDto.getPubId());
        validateDuplicatedWaiting(waitingList, pub);


        Waiting waiting = save(waitingRequestDto, pub.getMaxWaitingNum() + 1, user, pub);
        return new WaitingResponseDto(waiting.getWaitingNum(),
                pub.getMaxWaitingNum() - pub.getEnterNum()
        );
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
    public void deleteWaiting(Integer waitingNum) {
        waitingRepository.deleteByWaitingNum(waitingNum);
    }

    public List<MyWaitingList> getWaitingList(User user) {
        return waitingRepository.findWaitingListByUserId(user.getId());
    }

    public List<AdminWaitingList> getAdminWaitingList(Long pubId) {
        return waitingRepository.findWaitingsByPubId(pubId);
    }

    private void validateDuplicatedWaiting(List<Waiting> waitingList, Pub pub) {
        if (waitingList.stream()
                .anyMatch(waiting -> waiting.getPub().equals(pub))) {
            throw new RuntimeException("이미 해당 주점에 대한 대기열이 존재합니다.");
        }
    }
}
