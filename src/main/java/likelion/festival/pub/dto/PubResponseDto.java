package likelion.festival.pub.dto;

import likelion.festival.pub.domain.Pub;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.checkerframework.checker.units.qual.A;

@Getter
@AllArgsConstructor
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
