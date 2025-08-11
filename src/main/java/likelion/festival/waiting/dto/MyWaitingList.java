package likelion.festival.waiting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyWaitingList {
    private Long waitingId;
    private Integer waitingNum;
    private Integer wholeWaitingNum;
    private Integer numsTeamsAhead;
    private Long pubId;
    private Integer visitorCount;
}
