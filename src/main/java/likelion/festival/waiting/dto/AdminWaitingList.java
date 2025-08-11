package likelion.festival.waiting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class AdminWaitingList {
    private Long id;
    private LocalDateTime createdAt;
    private Integer waitingNum;
    private Integer visitorCount;
    private String phoneNumber;
    private String type;
}
