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

    // ** 파일 형식을 받지 않고, 파일이 저장된 url을 문자열로 받아와야 합니다.
    private String image;

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

    public void setImage(String image) {
        this.image = image;
    }
}
