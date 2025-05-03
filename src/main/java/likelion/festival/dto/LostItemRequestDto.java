package likelion.festival.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import likelion.festival.domain.LostItem;
import likelion.festival.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class LostItemRequestDto {
    //private MultipartFile image;
    @NotBlank(message = "name은 필수입니다.")
    private String name;

    @NotBlank(message = "description은 필수입니다.")
    private String description;

    @NotNull(message = "staffNotified은 필수입니다.")
    private Boolean staffNotified;

    @NotBlank(message = "foundLocation은 필수입니다.")
    private String foundLocation;

    @NotBlank(message = "foundDate은 필수입니다.")
    private String foundDate;

    @NotBlank(message = "foundTime은 필수입니다.")
    private String foundTime;
    //private Long userId;
}
