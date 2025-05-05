package likelion.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyWaitingList {
    private Long waitingId;
    private Integer wholeWaitingNum;
    private Integer numsTeamsAhead;
    private Long pubId;
    private Integer visitorCount;
}
