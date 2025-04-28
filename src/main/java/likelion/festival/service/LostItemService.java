package likelion.festival.service;

import likelion.festival.domain.LostItem;
import likelion.festival.dto.LostItemListResponseDto;
import likelion.festival.repository.LostItemRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LostItemService {
    private final LostItemRepository lostItemRepository;

    public List<LostItemListResponseDto> findByLostDate(String lostDate) {
        List<LostItem> lostItems = lostItemRepository.findByLostDate(lostDate);
        return lostItems.stream().map(lostItem -> new LostItemListResponseDto(lostItem))
                .toList();
    }

    public List<LostItemListResponseDto> findByLostDateAndName(String lostDate, String name) {
        List<LostItem> lostItems = lostItemRepository.findByLostDateAndNameContaining(lostDate, name);
        return lostItems.stream().map(lostItem -> new LostItemListResponseDto(lostItem))
                .toList();
    }
}
