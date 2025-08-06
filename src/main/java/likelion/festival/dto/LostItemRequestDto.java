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
    private String image; // 분실물 이미지 파일의 url


    @NotBlank(message = "name은 필수입니다.")
    private String name; // 분실물 등록 글 제목

    @NotBlank(message = "description은 필수입니다.")
    private String description; // 분실물 설명

    @NotNull(message = "staffNotified은 필수입니다.")
    private Boolean staffNotified; // 분실물을 스태프에게 전달했는지 여부

    @NotBlank(message = "foundLocation은 필수입니다.")
    private String foundLocation; // 분실물을 찾은 장소

    @NotBlank(message = "foundDate은 필수입니다.")
    private String foundDate; // 분실물을 찾은 날짜 (1일차, 2일차, 3일차)

    @NotBlank(message = "foundTime은 필수입니다.")
    private String foundTime; // 분실물을 찾은 시간 (ex. 13:00~14:00)

    public void setImage(String image) {
        this.image = image;
    }
}
