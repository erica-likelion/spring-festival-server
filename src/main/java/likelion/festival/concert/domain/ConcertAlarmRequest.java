package likelion.festival.concert.domain;

import jakarta.persistence.*;
import likelion.festival.user.domain.User;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ConcertAlarmRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artistName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public ConcertAlarmRequest(String artistName, User user) {
        this.artistName = artistName;
        this.user = user;
    }
}
