package likelion.festival.dto;

import likelion.festival.domain.Pub;
import lombok.Getter;

@Getter
public class PubResponseDto {
    private Long id;
    private String name;
    private Long likeCount;

    public PubResponseDto(Pub pub) {
        this.id = pub.getId();
        this.name = pub.getName();
        this.likeCount = pub.getLikeCount();
    }
}
