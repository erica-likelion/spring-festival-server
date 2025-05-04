package likelion.festival.dto;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class AdminWaitingList {
    private Long id;
    private LocalDateTime createdAt;
    private Integer waitingNum;
    private Integer visitorCount;
    private String phoneNumber;
    private String type;
}
