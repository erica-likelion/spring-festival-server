package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Pub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;

    private Long likeCount;

    @OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
    private List<Waiting> waitingList;
}
