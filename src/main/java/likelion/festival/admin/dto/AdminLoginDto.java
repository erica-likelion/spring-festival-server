package likelion.festival.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminLoginDto {
    private String username;
    private String password;
}
