package likelion.festival.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GuestWaitingRequestDto {

    @NotNull(message = "pubId는 필수 입력 속성입니다.")
    private Long pubId;

    @NotNull(message = "visitorCount는 필수 입력 속성입니다.")
    private Integer visitorCount;

    @NotBlank(message = "phoneNumber는 필수 입력 속성입니다.")
    private String phoneNumber;
}
