package likelion.festival.service;

import likelion.festival.domain.LikeRequestLog;
import likelion.festival.domain.Pub;
import likelion.festival.dto.PubResponseDto;
import likelion.festival.exceptions.EntityNotFoundException;
import likelion.festival.exceptions.PubException;
import likelion.festival.repository.LikeRequestLogRepository;
import likelion.festival.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;
    private final LikeRequestLogRepository likeRequestLogRepository;

    @Transactional
    public Pub getPubById(Long id) {
        return pubRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No pub with id: " + id));
    }

    public Pub getPubByName(String name) {
        return pubRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("해당 이름을 가진 주점이 존재하지 않습니다.: " + name));
    }

    public List<PubResponseDto> getPubRanks() {
        return pubRepository.findAllOrderById();
    }

    public List<Pub> getAllPubs() {
        return pubRepository.findAll();
    }

    @Transactional
    public void addPubLike(Long pubId, Integer addCount) {
        Pub pub = pubRepository.findById(pubId)
                .orElseThrow(() -> new EntityNotFoundException("요청한 id를 가진 주점을 찾을 수 없습니다: " + pubId));
        pub.addLikeCount(addCount);
    }

    @Transactional
    public void updateEnterNum(Integer waitingNum, Long pubId) {
        pubRepository.incrementEnterNum(waitingNum, pubId);
    }

    public Integer getTotalWaiting(Long pubId) {
        Pub pub = pubRepository.findPubWithWaitingsAndGuestWaitings(pubId)
                .orElseThrow(() -> new EntityNotFoundException("해당 주점이 존재하지 않습니다. " + pubId));
        return pub.getGuestWaitingList().size() + pub.getWaitingList().size();
    }

    @Transactional
    public boolean checkLikeCount(String ip, LocalDateTime time) {
        if (!likeRequestLogRepository.existsByIp(ip)) {
            likeRequestLogRepository.save(new LikeRequestLog(ip, false, time));
            return true;
        }
        LikeRequestLog requestIp = likeRequestLogRepository.findByIp(ip).
                orElseThrow(() -> new EntityNotFoundException("해당 ip를 찾을 수 없음"));
        Long term = Math.abs(ChronoUnit.SECONDS.between(time, requestIp.getSendTime()));
        if (requestIp.getRedLight()) {
            return false;
        }
        if (term < 20) {
            requestIp.setRedLight(true);
            requestIp.setSendTime(time);
            return false;
        }
        requestIp.setSendTime(time);
        return true;
    }private String ip;

    private Boolean redLight;

    private LocalDateTime sendTime;
}
