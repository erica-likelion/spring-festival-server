package likelion.festival.controller;

import likelion.festival.dto.LostItemListResponseDto;
import likelion.festival.service.LostItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lost-items")
public class LostItemController {

    private final LostItemService lostItemService;

    @GetMapping
    public List<LostItemListResponseDto> getLostItems(@RequestParam String lostDate, @RequestParam(required = false) String name) {
        if (name == null || name.isBlank()) { // 모든 분실물을 반환
            return lostItemService.findByLostDate(lostDate);
        } else {
            return lostItemService.findByLostDateAndName(lostDate, name);
        }

    }
}
