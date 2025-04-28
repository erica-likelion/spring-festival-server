package likelion.festival.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import likelion.festival.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LostItemDetailResponseDto {
    private Long id;
    private String image;
    private String name;
    private String description;
    private Boolean staffNotified;
    private String foundLocation;
    private String foundDate;
    private String foundTime;
}
