package likelion.festival.service;

import likelion.festival.domain.LostItem;
import likelion.festival.dto.LostItemResponseDto;
import likelion.festival.dto.LostItemRequestDto;
import likelion.festival.repository.LostItemRepository;
import likelion.festival.repository.UserRepository;
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

    public List<LostItemResponseDto> allLostItem() {
        List<LostItem> lostItems = lostItemRepository.findAll();
        return lostItems.stream().map(lostItem -> new LostItemResponseDto(lostItem))
                .toList();
    }

    public List<LostItemResponseDto> findByLostDate(String lostDate) {
        List<LostItem> lostItems = lostItemRepository.findByFoundDate(lostDate);
        return lostItems.stream().map(lostItem -> new LostItemResponseDto(lostItem))
                .toList();
    }

    public List<LostItemResponseDto> findByName(String name) {
        List<LostItem> lostItems = lostItemRepository.findByNameContaining(name);
        return lostItems.stream().map(lostItem -> new LostItemResponseDto(lostItem))
                .toList();
    }

    public List<LostItemResponseDto> findByLostDateAndName(String lostDate, String name) {
        List<LostItem> lostItems = lostItemRepository.findByFoundDateAndNameContaining(lostDate, name);
        return lostItems.stream().map(lostItem -> new LostItemResponseDto(lostItem))
                .toList();
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
