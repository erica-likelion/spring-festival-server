package likelion.festival.service;

import likelion.festival.domain.Pub;
import likelion.festival.domain.User;
import likelion.festival.domain.Waiting;
import likelion.festival.dto.WaitingRequestDto;
import likelion.festival.dto.WaitingResponseDto;
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
    public WaitingResponseDto addWaiting(User user, WaitingRequestDto waitingRequestDto) {
        List<Waiting> waitingList = user.getWaitingList();
        if (waitingList.size() > 3) {
            throw new IllegalArgumentException("");
        }

        Pub pub = pubService.getPubById(waitingRequestDto.getPubId());
        validateDuplicatedWaiting(waitingList, pub);


        Waiting waiting = save(waitingRequestDto, pub.getWaitingNum() + 1, user, pub);
        return new WaitingResponseDto(waiting.getWaitingNum(),
                pub.getWaitingNum() - pub.getEnterNum()
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

    private void validateDuplicatedWaiting(List<Waiting> waitingList, Pub pub) {
        if (waitingList.stream()
                .anyMatch(waiting -> waiting.getPub().equals(pub))) {
            throw new RuntimeException("이미 해당 주점에 대한 대기열이 존재합니다.");
        }
    }
}
