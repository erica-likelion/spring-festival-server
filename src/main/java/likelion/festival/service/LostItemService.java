package likelion.festival.service;

import likelion.festival.domain.LostItem;
import likelion.festival.domain.User;
import likelion.festival.dto.LostItemDetailResponseDto;
import likelion.festival.dto.LostItemListResponseDto;
import likelion.festival.dto.LostItemRequestDto;
import likelion.festival.exceptions.EntityNotFoundException;
import likelion.festival.exceptions.InvalidRequestException;
import likelion.festival.repository.LostItemRepository;
import likelion.festival.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LostItemService {
    private final LostItemRepository lostItemRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    public List<LostItemListResponseDto> findByLostDate(String lostDate) {
        List<LostItem> lostItems = lostItemRepository.findByFoundDate(lostDate);
        return lostItems.stream().map(lostItem -> new LostItemListResponseDto(lostItem))
                .toList();
    }

    public List<LostItemListResponseDto> findByLostDateAndName(String lostDate, String name) {
        List<LostItem> lostItems = lostItemRepository.findByFoundDateAndNameContaining(lostDate, name);
        return lostItems.stream().map(lostItem -> new LostItemListResponseDto(lostItem))
                .toList();
    }

    public LostItemDetailResponseDto findLostItem(Long lostItemId) {
        LostItem lostItem = lostItemRepository.findById(lostItemId)
                .orElseThrow(() -> new EntityNotFoundException("요청한 id를 가진 분실물이 존재하지 않습니다: " + lostItemId));
        return new LostItemDetailResponseDto(lostItem);
    }

    @Transactional
    public LostItem addLostItem(LostItemRequestDto dto, MultipartFile image) {
        String imageUrl = imageService.saveImage(image);

        LostItem lostItem = new LostItem(
                imageUrl,
                dto.getName(),
                dto.getDescription(),
                dto.getStaffNotified(),
                dto.getFoundLocation(),
                dto.getFoundDate(),
                dto.getFoundTime()
        );

        LostItem savedLostItem = lostItemRepository.save(lostItem);
        return savedLostItem;
    }
}
