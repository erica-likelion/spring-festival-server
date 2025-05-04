package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Pub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //private String host;

    @Column(unique = true)
    private String name;

    private Long likeCount;

    private Integer enterNum;

    private Integer maxWaitingNum;

    private String password;

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
