package likelion.festival.dto;

import likelion.festival.domain.GuestWaiting;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GuestWaitingResponseDto {
    private Long id;

    private Integer visitorCount;

    private String phoneNumber;

    private Integer waitingNum;

    public GuestWaitingResponseDto(GuestWaiting guestWaiting) {
        this.id = guestWaiting.getId();
        this.visitorCount = guestWaiting.getVisitorCount();
        this.phoneNumber = guestWaiting.getPhoneNumber();
        this.waitingNum = guestWaiting.getWaitingNum();
    }
}
