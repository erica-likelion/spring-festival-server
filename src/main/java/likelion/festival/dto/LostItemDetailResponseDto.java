package likelion.festival.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import likelion.festival.domain.LostItem;
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

    public LostItemDetailResponseDto(LostItem lostItem) {
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
