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

    public Pub getPubByName(String name) {
        return pubRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("해당 이름을 가진 주점이 존재하지 않습니다.: " + name));
    }

    public List<PubResponseDto> getPubRanks() {
        List<Pub> pubs = pubRepository.findAllByOrderByLikeCountDesc();
        return pubs.stream().map(pub -> new PubResponseDto(pub))
                .toList();
    }

    @Transactional
    public void addPubLike(Long pubId, Integer addCount) {
        Pub pub = pubRepository.findById(pubId)
                .orElseThrow(() -> new EntityNotFoundException("요청한 id를 가진 주점을 찾을 수 없습니다: " + pubId));
        pub.addLikeCount(addCount);
    }
}
