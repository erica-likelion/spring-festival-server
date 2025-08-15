package likelion.festival.lostitem.controller;

import jakarta.validation.Valid;
import likelion.festival.exceptions.global.InvalidRequestException;
import likelion.festival.lostitem.domain.LostItem;
import likelion.festival.lostitem.dto.LostItemResponseDto;
import likelion.festival.lostitem.dto.LostItemRequestDto;
import likelion.festival.lostitem.service.LostItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Arrays;
import java.util.List;


/*
* 분실물과 관련된 api 요청을 받는 컨트롤러
*   - GET /api/lost-items
*   - POST /api/lost-items
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lost-items")
public class LostItemController {

    private final LostItemService lostItemService;

    /*
    * method: GET
    * url: /api/lost-items
    * query parameter:
    *   - lostDate: 분실 날짜 ("1일차", "2일차", "3일차" 중 택1)
    *   - name: 분실물 이름 검색어
    * response body: LostItemResponseDto[]
    */
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

    /*
     * method: POST
     * url: /api/lost-items
     * request body(martipart-form):
     *  - LostItemRequestDto(json)
     *  - image(file)
     */
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

    /* 불실날짜 형식 검증 메소드:
    * 분실 날짜 형식은 "1일차", "2일차", "3일차" 중 하나로 전달
    * */
    private void validateFestivalDate(String date) {
        List<String> possibleDates = Arrays.asList("1일차", "2일차", "3일차");
        if (date != null && !possibleDates.contains(date)) {
            throw new InvalidRequestException("lostDate는 ['1일차', '2일차', '3일차'] 중 하나를 입력해야합니다.");
        }
    }
}
