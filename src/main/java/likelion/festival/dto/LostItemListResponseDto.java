package likelion.festival.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LostItemListResponseDto {
    private Long id;
    private String image;
    private String name;
    private Boolean staffNotified;
    private String foundLocation;
    private String foundDate;
}
