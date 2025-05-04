package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Pub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;

    private Long likeCount;

    private Integer enterNum;

    private Integer maxWaitingNum;

    @OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
    private List<Waiting> waitingList;

    @OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
    private List<GuestWaiting> guestWaitingList;

    public Integer addWaitingNum() {
        this.maxWaitingNum += 1;
        return this.maxWaitingNum;
    }

    public void addLikeCount(Long addCount) {
        this.likeCount += addCount;
    }
}
