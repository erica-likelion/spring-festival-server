package likelion.festival.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WaitingAlarmRequest {
    @NotBlank(message = "토큰 값이 없습니다")
    private String token;

    @NotBlank(message = "주점 이름이 없습니다")
    private String pubName;
}
