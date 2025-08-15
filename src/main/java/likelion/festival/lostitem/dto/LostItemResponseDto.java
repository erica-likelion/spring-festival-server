package likelion.festival.lostitem.dto;

import likelion.festival.lostitem.domain.LostItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LostItemResponseDto {
    private Long id; // pk
    private String image; // 분실물 이미지 파일의 url
    private String name; // 분실물 등록 글 제목
    private String description; // 분실물 설명
    private Boolean staffNotified; // 분실물을 스태프에게 전달했는지 여부
    private String foundLocation; // 분실물을 찾은 장소
    private String foundDate; // 분실물을 찾은 날짜 (1일차, 2일차, 3일차)
    private String foundTime; // 분실물을 찾은 시간 (ex. 13:00~14:00)

    /*
    * LostItem 객체 -> dto 변환
    * */
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
