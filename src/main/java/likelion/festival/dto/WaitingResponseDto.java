package likelion.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WaitingResponseDto {
    private Integer waitingNum;
    private Integer numsTeamsAhead;
}
