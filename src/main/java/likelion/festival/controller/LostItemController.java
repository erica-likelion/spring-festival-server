package likelion.festival.controller;

import jakarta.validation.Valid;
import likelion.festival.domain.LostItem;
import likelion.festival.dto.LostItemResponseDto;
import likelion.festival.dto.LostItemRequestDto;
import likelion.festival.exceptions.InvalidRequestException;
import likelion.festival.service.LostItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lost-items")
public class LostItemController {

    private final LostItemService lostItemService;

    @GetMapping
    public ResponseEntity<List<LostItemResponseDto>> getLostItems(@RequestParam(required = false) String lostDate, @RequestParam(required = false) String name) {
        List<LostItemResponseDto>lostItems;

        validateFestivalDate(lostDate);

        if (lostDate == null) {
            if (name == null) { // 모든 분실물 반환
                lostItems = lostItemService.allLostItem();
            } else { // 검색어만 있을때
                lostItems = lostItemService.findByName(name);
            }
        } else {
            if (name == null) { // 분실 날짜만 있음
                lostItems = lostItemService.findByLostDate(lostDate);
            } else { // 날짜, 검색어 모두 있음
                lostItems = lostItemService.findByLostDateAndName(lostDate, name);
            }
        }
        return ResponseEntity.ok(lostItems);
    }

    @PostMapping
    public ResponseEntity<Void> addLostItem(@Valid @RequestPart("data") LostItemRequestDto dto, @RequestPart("image") MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new InvalidRequestException("분실물 이미지가 첨부되지 않았습니다.");
        }

        validateFestivalDate(dto.getFoundDate());

        LostItem lostItem = lostItemService.addLostItem(dto, image);
        URI location = URI.create("/api/lost-items/" + lostItem.getId());
        return ResponseEntity.created(location).build();
    }

    private void validateFestivalDate(String date) {
        List<String> possibleDates = Arrays.asList("1일차", "2일차", "3일차");
        if (date != null && !possibleDates.contains(date)) {
            throw new InvalidRequestException("lostDate는 ['1일차', '2일차', '3일차'] 중 하나를 입력해야합니다.");
        }
    }
}
