package likelion.festival.domain;

import jakarta.persistence.*;

@Entity
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
