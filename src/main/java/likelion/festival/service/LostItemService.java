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

/*
* LostItem 객체의 기본 서비스 클래스
* 모든 분실물 조회, 분실 날짜에 따른 필터링과 이름으로 검색하는 로직을 포함
* 분실물을 새로 등록하는 로직
* */
@Service
@Transactional
@RequiredArgsConstructor
public class LostItemService {
    private final LostItemRepository lostItemRepository;
    private final NcpObjectStorageService ncpObjectStorageService;


    /*
    * 모든 분실물 정보를 리스트 조회
    * output:
    *   - "LostItemResponseDto"를 담은 리스트
    */
    public List<LostItemResponseDto> allLostItem() {
        List<LostItem> lostItems = lostItemRepository.findAll();
        return lostItems.stream().map(lostItem -> new LostItemResponseDto(lostItem))
                .toList();
    }

    /*
    * 특정 분실 날짜에 해당하는 분실물들만 따로 리스트 조회
    * input:
    *   - lostDate: ["1일차", "2일차", "3일차"] 중 하나에 해당
    * output:
    *   - "LostItemResponseDto"를 담은 리스트
    */
    public List<LostItemResponseDto> findByLostDate(String lostDate) {
        List<LostItem> lostItems = lostItemRepository.findByFoundDate(lostDate);
        return lostItems.stream().map(lostItem -> new LostItemResponseDto(lostItem))
                .toList();
    }

    /*
    * 분실물(LostItem) 중에  "name"속성이 검색어를 포함하고 있는 경우만 필터링해서 조회
    * input:
    *   - name: 검색하고자 하는 분실물 이름
    * output:
    *   - "LostItemResponseDto"를 담은 리스트
    */
    public List<LostItemResponseDto> findByName(String name) {
        List<LostItem> lostItems = lostItemRepository.findByNameContaining(name);
        return lostItems.stream().map(lostItem -> new LostItemResponseDto(lostItem))
                .toList();
    }

    /*
    * 분실 날짜와 분실물 이름 조건에 모두 해당하는 분실물만 필터링해서 조회
    * input:
    *   - lostDate: ["1일차", "2일차", "3일차"] 중 하나에 해당
    *   - name: 검색하고자 하는 분실물 이름
    * return:
    *   - "LostItemResponseDto"를 담은 리스트
    */
    public List<LostItemResponseDto> findByLostDateAndName(String lostDate, String name) {
        List<LostItem> lostItems = lostItemRepository.findByFoundDateAndNameContaining(lostDate, name);
        return lostItems.stream().map(lostItem -> new LostItemResponseDto(lostItem))
                .toList();
    }

    /*
    * input:
    *   - dto: "image" 필드가 비어있는 LostItemRequestDto
    *   - image: 사용자가 보낸 분실물 이미지 파일
    * return:
    *   - DB에 저장된 분실물 객체 반환
    */
    public LostItem addLostItem(LostItemRequestDto dto, MultipartFile image) {
        String imageKey;
        try {
            imageKey = ncpObjectStorageService.uploadImage(image);
        } catch (IOException e) {
            throw new InvalidRequestException("이미지 업로드에 실패했습니다.");
        }
        dto.setImage(imageKey);
        LostItem lostItem = new LostItem(dto);
        LostItem savedLostItem = lostItemRepository.save(lostItem);
        return savedLostItem;
    }
}
