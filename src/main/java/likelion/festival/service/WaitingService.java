package likelion.festival.service;

import likelion.festival.domain.GuestWaiting;
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
import likelion.festival.repository.GuestWaitingRepository;
import likelion.festival.repository.PubRepository;
import likelion.festival.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final PubService pubService;
    private final PubRepository pubRepository;

    @Transactional
    public WaitingResponseDto addWaiting(User user, WaitingRequestDto waitingRequestDto){
        List<Waiting> waitingList = waitingRepository.findWaitingListByUserId(user.getId());
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
                waitingNum,
                user,
                pub));
    }

    public Waiting getWaiting(Long waitingId) {
        return waitingRepository.findById(waitingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 웨이팅이 존재하지 않습니다: " + waitingId));
    }

    // 사용자가 직접 삭제
    @Transactional
    public void deleteWaiting(Long waitingId) {
        Waiting waiting = getWaiting(waitingId);
        waitingRepository.delete(waiting);
    }

    // 관리자에서 노쇼, 입장으로 웨이팅을 삭제하는 경우
    @Transactional
    public void deleteWaiting(Waiting waiting) {
        pubService.updateEnterNum(waiting.getWaitingNum(), waiting.getPub().getId());
        waitingRepository.delete(waiting);
    }

    public List<MyWaitingList> getWaitingList(User user) {
        List<Waiting> waitingList = user.getWaitingList();

        return waitingList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private MyWaitingList convertToDto(Waiting waiting) {
        Pub pub = pubRepository.findPubWithWaitingsAndGuestWaitings(waiting.getPub().getId());
        Set<Waiting> pubWaitings = pub.getWaitingList();
        Set<GuestWaiting> pubGuestWaitings = pub.getGuestWaitingList();

        int totalTeams = pubWaitings.size() + pubGuestWaitings.size();
        long aheadCount = calculateAheadCount(waiting.getCreatedAt(), pub);

        return MyWaitingList.builder()
                .waitingNum(waiting.getWaitingNum())
                .wholeWaitingNum(totalTeams)
                .numsTeamsAhead((int) aheadCount)
                .pubId(pub.getId())
                .visitorCount(waiting.getVisitorCount())
                .build();
    }

    private long calculateAheadCount(LocalDateTime targetTime, Pub pub) {
        long waitingCount = pub.getWaitingList().stream()
                .filter(w -> w.getCreatedAt().isBefore(targetTime))
                .count();

        long guestCount = pub.getGuestWaitingList().stream()
                .filter(gw -> gw.getCreatedAt().isBefore(targetTime))
                .count();

        return waitingCount + guestCount + 1;
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
