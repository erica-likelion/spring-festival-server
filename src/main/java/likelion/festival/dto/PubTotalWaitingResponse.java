package likelion.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PubTotalWaitingResponse {
    private Long id;
    private Integer waitingCount;
}
