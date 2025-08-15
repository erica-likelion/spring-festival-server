package likelion.festival.pub.domain;

import jakarta.persistence.*;
import likelion.festival.waiting.domain.GuestWaiting;
import likelion.festival.waiting.domain.Waiting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Pub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Long likeCount;

    private Integer enterNum;

    private Integer maxWaitingNum;

    private String password;

    @OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
    private Set<Waiting> waitingList;

    @OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
    private Set<GuestWaiting> guestWaitingList;

    public Integer addWaitingNum() {
        this.maxWaitingNum += 1;
        return this.maxWaitingNum;
    }

    public void addLikeCount(Integer addCount) {
        this.likeCount += addCount;
    }
}
