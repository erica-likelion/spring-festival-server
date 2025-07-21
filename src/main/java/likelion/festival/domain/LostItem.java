package likelion.festival.domain;

import jakarta.persistence.*;
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

    // 분실물 생성 api에서 사용하는 기본 생성자
    public LostItem(
            String image,
            String name,
            String description,
            Boolean staffNotified,
            String foundLocation,
            String foundDate,
            String foundTime
    ) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.staffNotified = staffNotified;
        this.foundLocation = foundLocation;
        this.foundDate = foundDate;
        this.foundTime = foundTime;
    }
}

