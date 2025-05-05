package likelion.festival.service;

import likelion.festival.domain.Pub;
import likelion.festival.dto.PubResponseDto;
import likelion.festival.exceptions.EntityNotFoundException;
import likelion.festival.exceptions.PubException;
import likelion.festival.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;

    @Transactional
    public Pub getPubById(Long id) {
        return pubRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No pub with id: " + id));
    }

    public List<PubResponseDto> getPubRanks() {
        List<Pub> pubs = pubRepository.findAllByOrderByLikeCountDesc();
        return pubs.stream().map(pub -> new PubResponseDto(pub))
                .toList();
    }

    @Transactional
    public void addPubLike(Long pubId, Long addCount) {
        Pub pub = pubRepository.findById(pubId)
                .orElseThrow(() -> new EntityNotFoundException("요청한 id를 가진 주점을 찾을 수 없습니다: " + pubId));
        pub.addLikeCount(addCount);
    }
}
