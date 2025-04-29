package likelion.festival.controller;

import likelion.festival.domain.LostItem;
import likelion.festival.dto.LostItemListResponseDto;
import likelion.festival.dto.LostItemRequestDto;
import likelion.festival.service.LostItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lost-items")
public class LostItemController {

    private final LostItemService lostItemService;

    @GetMapping
    public ResponseEntity<List<LostItemListResponseDto>> getLostItems(@RequestParam String lostDate, @RequestParam(required = false) String name) {
        List<LostItemListResponseDto>lostItems;

        if (name == null || name.isBlank()) { // 모든 분실물을 반환
            lostItems = lostItemService.findByLostDate(lostDate);
        } else {
            lostItems = lostItemService.findByLostDateAndName(lostDate, name);
        }
        return ResponseEntity.ok(lostItems);
    }

    @PostMapping
    public ResponseEntity<Void> addLostItem(@RequestPart("data") LostItemRequestDto dto, @RequestPart("image") MultipartFile image) {
        System.out.println(dto);

        LostItem lostItem = lostItemService.addLostItem(dto, image);
        URI location =URI.create("/api/lost-items/" + lostItem.getId());
        return ResponseEntity.created(location).build();
    }
}
