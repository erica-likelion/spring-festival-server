package likelion.festival.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthCodeRequestDto {
    @NotNull(message = "인증 코드 값이 없습니다")
    private String code;
}
