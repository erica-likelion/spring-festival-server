package likelion.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WaitingRequestDto {
    private Long visitorCount;

    private String phoneNumber;

    private String pubName;

    private Long pubId;

    private Long userId;
}
