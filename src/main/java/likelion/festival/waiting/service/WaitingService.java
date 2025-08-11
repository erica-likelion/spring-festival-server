package likelion.festival.waiting.service;

import likelion.festival.exceptions.global.EntityNotFoundException;
import likelion.festival.pub.exception.PubException;
import likelion.festival.waiting.domain.GuestWaiting;
import likelion.festival.pub.domain.Pub;
import likelion.festival.pub.service.PubService;
import likelion.festival.user.domain.User;
import likelion.festival.waiting.domain.Waiting;
import likelion.festival.waiting.dto.AdminWaitingList;
import likelion.festival.waiting.dto.MyWaitingList;
import likelion.festival.waiting.dto.WaitingRequestDto;
import likelion.festival.waiting.dto.WaitingResponseDto;
import likelion.festival.pub.repository.PubRepository;
import likelion.festival.waiting.exception.WaitingException;
import likelion.festival.waiting.repository.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 웨이팅 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 *
 * @author 김승민
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final PubService pubService;
    private final PubRepository pubRepository;

    /**
     * 새로운 웨이팅을 추가합니다.
     *
     * @param user 로그인 사용자
     * @param waitingRequestDto 웨이팅 생성 요청 데이터
     * @return 생성된 웨이팅의 응답 데이터
     * @throws WaitingException 웨이팅 목록이 가득 찼을 경우 발생
     * @throws PubException 이미 해당 주점에 대한 대기열이 존재할 경우 발생
     */
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

    /**
     * 웨이팅 정보를 데이터베이스에 저장합니다.
     *
     * @param waitingRequestDto 웨이팅 생성 요청 데이터
     * @param waitingNum 할당된 웨이팅 번호
     * @param user 웨이팅을 신청한 사용자
     * @param pub 웨이팅 대상 주점
     * @return 저장된 {@link Waiting} 엔티티
     */
    @Transactional
    public Waiting save(WaitingRequestDto waitingRequestDto, Integer waitingNum, User user, Pub pub) {
        return waitingRepository.save(new Waiting(waitingRequestDto.getVisitorCount(),
                waitingRequestDto.getPhoneNumber(),
                waitingNum,
                user,
                pub));
    }

    /**
     * ID를 통해 웨이팅 정보를 조회합니다.
     *
     * @param waitingId 조회할 웨이팅의 ID
     * @return 조회된 {@link Waiting} 엔티티
     * @throws EntityNotFoundException 해당 ID의 웨이팅이 존재하지 않을 경우 발생
     */
    public Waiting getWaiting(Long waitingId) {
        return waitingRepository.findById(waitingId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id를 가진 웨이팅이 존재하지 않습니다: " + waitingId));
    }

    /**
     * 사용자가 직접 자신의 웨이팅을 삭제합니다.
     *
     * @param waitingId 삭제할 웨이팅의 ID
     */
    @Transactional
    public void deleteWaiting(Long waitingId) {
        Waiting waiting = getWaiting(waitingId);
        waitingRepository.delete(waiting);
    }

    /**
     * 주점 관리자가 특정 웨이팅을 삭제하고 현재 데이터로 업데이트합니다.
     *
     * @param waiting 삭제할 웨이팅 엔티티
     */
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
        Long pubId = waiting.getPub().getId();
        Pub pub = pubRepository.findPubWithWaitingsAndGuestWaitings(pubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 주점이 존재하지 않습니다." + pubId));
        Set<Waiting> pubWaitings = pub.getWaitingList();
        Set<GuestWaiting> pubGuestWaitings = pub.getGuestWaitingList();

        int totalTeams = pubWaitings.size() + pubGuestWaitings.size();
        long aheadCount = calculateAheadCount(waiting.getCreatedAt(), pub);

        return MyWaitingList.builder()
                .waitingId(waiting.getId())
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
