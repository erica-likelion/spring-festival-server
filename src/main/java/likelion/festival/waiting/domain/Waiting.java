package likelion.festival.waiting.domain;

import jakarta.persistence.*;
import likelion.festival.user.domain.User;
import likelion.festival.pub.domain.Pub;
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

    private String phoneNumber;


    private Integer waitingNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;

    public Waiting(Integer visitorCount, String phoneNumber, Integer waitingNum, User user, Pub pub) {
        this.visitorCount = visitorCount;
        this.phoneNumber = phoneNumber;
        this.waitingNum = waitingNum;
        this.createdAt = LocalDateTime.now();
        this.user = user;
        this.pub = pub;
    }
}
