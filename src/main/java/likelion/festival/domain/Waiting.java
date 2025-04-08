package likelion.festival.domain;

import jakarta.persistence.*;
import likelion.festival.enums.WaitingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private Long visitorCount;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;

    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pub_id")
    private Pub pub;
}
