package likelion.festival.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PubRequestDto {

    @NotNull(message = "addCount는 필수 필드입니다.")
    @Positive(message = "addCount는 반드시 양수로 입력해야합니다.")
    private Long addCount;
}
