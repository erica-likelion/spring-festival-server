package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class GuestWaiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer visitorCount;

    private String phoneNumber;

    private Integer waitingNum;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;

    public GuestWaiting(Integer visitorCount, String phoneNumber, Integer waitingNum,Pub pub) {
        this.visitorCount = visitorCount;
        this.phoneNumber = phoneNumber;
        this.waitingNum = waitingNum;
        this.createdAt = LocalDateTime.now();
        this.pub = pub;
    }
}
