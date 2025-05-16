package likelion.festival.dto;

import likelion.festival.domain.LostItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LostItemResponseDto {
    private Long id;
    private String image;
    private String name;
    private String description;
    private Boolean staffNotified;
    private String foundLocation;
    private String foundDate;
    private String foundTime;

    public LostItemResponseDto(LostItem lostItem) {
        this.id = lostItem.getId();
        this.image = lostItem.getImage();
        this.name = lostItem.getName();
        this.description = lostItem.getDescription();
        this.staffNotified = lostItem.getStaffNotified();
        this.foundLocation = lostItem.getFoundLocation();
        this.foundDate = lostItem.getFoundDate();
        this.foundTime = lostItem.getFoundTime();
    }
}
