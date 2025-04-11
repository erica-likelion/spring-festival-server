package likelion.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthCodeRequestDto {
    private String code;
    private String error;
    private String error_description;
    private String state;
}
