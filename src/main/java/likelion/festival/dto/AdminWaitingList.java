package likelion.festival.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class AdminWaitingList {
    private LocalDateTime createdAt;
    private Integer waitingNum;
    private Integer visitorCount;
    private String phoneNumber;
}
