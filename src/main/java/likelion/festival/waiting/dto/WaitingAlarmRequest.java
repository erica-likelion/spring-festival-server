package likelion.festival.waiting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WaitingAlarmRequest {
    @NotBlank(message = "주점 이름이 없습니다")
    private String pubName;

    private Long waitingId;

    private String type;
}
