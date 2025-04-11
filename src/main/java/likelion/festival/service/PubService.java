package likelion.festival.service;

import likelion.festival.domain.Pub;
import likelion.festival.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PubService {
    private final PubRepository pubRepository;

    @Transactional
    public Pub getPubById(Long id) {
        return pubRepository.findById(id).orElseThrow();
    }
}
