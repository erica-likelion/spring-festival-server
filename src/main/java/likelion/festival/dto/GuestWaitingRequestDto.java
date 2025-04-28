package likelion.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GuestWaitingRequestDto {
    private Long pubId;

    private Integer visitorCount;

    private String phoneNumber;
}
