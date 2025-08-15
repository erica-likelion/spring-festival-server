package likelion.festival.pub.controller;

import likelion.festival.pub.domain.Pub;
import likelion.festival.pub.dto.PubRequestDto;
import likelion.festival.pub.dto.PubResponseDto;
import likelion.festival.pub.dto.PubTotalWaitingResponse;
import likelion.festival.pub.service.PubService;
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
    public synchronized ResponseEntity<List<PubResponseDto>> addLike(@RequestBody List<PubRequestDto> dtoList) {
        for (PubRequestDto dto : dtoList) {
            pubService.addPubLike(dto.getPubId(), dto.getAddCount());
        }
        List<PubResponseDto> pubs = pubService.getPubRanks();
        return ResponseEntity.ok(pubs);
    }

    @GetMapping("/waiting-count")
    public ResponseEntity<List<PubTotalWaitingResponse>> getPubTotalWaitingCount() {
        List<Pub> pubs = pubService.getAllPubs();
        List<PubTotalWaitingResponse> response =  pubs.stream().map(pub -> new PubTotalWaitingResponse(
                pub.getId(),
                pubService.getTotalWaiting(pub.getId())
        )).toList();
        return ResponseEntity.ok(response);
    }
}
