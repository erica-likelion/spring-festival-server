package likelion.festival.domain;

import jakarta.persistence.*;
import likelion.festival.enums.WaitingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private Integer visitorCount;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    private String phoneNumber;

    private String pubName;

    private Integer waitingNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;

    public Waiting(Integer visitorCount, String phoneNumber, String pubName, Integer waitingNum, User user, Pub pub) {
        this.visitorCount = visitorCount;
        this.phoneNumber = phoneNumber;
        this.pubName = pubName;
        this.waitingNum = waitingNum;
        this.createdAt = LocalDateTime.now();
        this.waitingStatus = WaitingStatus.Wait;
        this.user = user;
        this.pub = pub;
    }
}
