package likelion.festival.controller;

import jakarta.servlet.http.HttpServletRequest;
import likelion.festival.domain.Pub;
import likelion.festival.dto.PubRequestDto;
import likelion.festival.dto.PubResponseDto;
import likelion.festival.dto.PubTotalWaitingResponse;
import likelion.festival.service.PubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public synchronized ResponseEntity<List<PubResponseDto>> addLike(
            @RequestBody List<PubRequestDto> dtoList,
            HttpServletRequest request
    ) {
        String requestIp = request.getRemoteAddr();
        Boolean rightRequest = true;
        if (requestIp != null && !pubService.checkLikeCount(requestIp, LocalDateTime.now())) {
            rightRequest = false;
        }
        for (PubRequestDto dto : dtoList) {
            if (dto.getAddCount() > 300 || !rightRequest) {
                pubService.addPubLike(dto.getPubId(), -300);
            } else {
                pubService.addPubLike(dto.getPubId(), dto.getAddCount());
            }
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
