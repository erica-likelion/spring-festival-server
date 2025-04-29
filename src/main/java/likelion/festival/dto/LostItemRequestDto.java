package likelion.festival.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import likelion.festival.domain.LostItem;
import likelion.festival.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class LostItemRequestDto {
    private MultipartFile image;
    private String name;
    private String description;
    private Boolean staffNotified;
    private String foundLocation;
    private String foundDate;
    private String foundTime;
    private Long userId;
}
