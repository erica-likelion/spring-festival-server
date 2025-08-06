package likelion.festival.domain;

import jakarta.persistence.*;
import likelion.festival.dto.LostItemRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
* 축제 기간 동안 발견된 분실물 정보를 저장하는 클래스입니다.
* 분실물의 사진과 설명, 발견 장소와 시간, 축제 스태프에게 전달 여부 등을 함께 저장합니다.
* */
@Entity
@Getter
@NoArgsConstructor
public class LostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String name;

    private String description;

    private Boolean staffNotified;

    private String foundLocation;

    private String foundDate;

    private String foundTime;

    // dto -> entity로 변환하는 생성자
    public LostItem(LostItemRequestDto dto) {
        this.image = dto.getImage();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.staffNotified = dto.getStaffNotified();
        this.foundLocation = dto.getFoundLocation();
        this.foundDate = dto.getFoundDate();
        this.foundTime = dto.getFoundTime();
    }
}