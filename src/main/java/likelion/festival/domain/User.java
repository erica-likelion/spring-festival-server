package likelion.festival.domain;

import jakarta.persistence.*;
import likelion.festival.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(unique = true)
    private String email;

    private String refreshToken;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
//    private List<LostItem> lostItems = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Waiting> waitingList = new ArrayList<>();

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
