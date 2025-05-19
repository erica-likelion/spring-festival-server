package likelion.festival.service;

import likelion.festival.domain.LostItem;
import likelion.festival.dto.LostItemResponseDto;
import likelion.festival.dto.LostItemRequestDto;
import likelion.festival.exceptions.InvalidRequestException;
import likelion.festival.repository.LostItemRepository;
import likelion.festival.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LostItemService {
    private final LostItemRepository lostItemRepository;
    private final UserRepository userRepository;
    private final NcpObjectStorageService ncpObjectStorageService;

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
        String imageKey;
        try {
            imageKey = ncpObjectStorageService.uploadImage(image);
        } catch (IOException e) {
            throw new InvalidRequestException("이미지 업로드에 실패했습니다.");
        }

        LostItem lostItem = new LostItem(
                imageKey,
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
