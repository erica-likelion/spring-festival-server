package likelion.festival.controller;

import likelion.festival.dto.PubRequestDto;
import likelion.festival.dto.PubResponseDto;
import likelion.festival.service.PubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pubs")
public class PubController {

    private final PubService pubService;

    @GetMapping
    public ResponseEntity<List<PubResponseDto>> getPubs() {
        List<PubResponseDto> pubs = pubService.getPubRanks();
        return ResponseEntity.ok(pubs);
    }

    @PostMapping("/like")
    public synchronized ResponseEntity<List<PubResponseDto>> addLike(@PathVariable Long pubId, @RequestBody List<PubRequestDto> dtoList) {
        for (PubRequestDto dto : dtoList) {
            pubService.addPubLike(dto.getPubId(), dto.getAddCount());
        }
        List<PubResponseDto> pubs = pubService.getPubRanks();
        return ResponseEntity.ok(pubs);
    }
}
