package likelion.festival.dto;

import likelion.festival.domain.LostItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LostItemListResponseDto {
    private Long id;
    private String image;
    private String name;
    private Boolean staffNotified;
    private String foundLocation;
    private String foundDate;

    public LostItemListResponseDto(LostItem entity) {
        this.id = entity.getId();
        this.image = entity.getImage();
        this.name = entity.getName();
        this.staffNotified = entity.getStaffNotified();
        this.foundLocation = entity.getFoundLocation();
        this.foundDate = entity.getFoundDate();
    }
}
