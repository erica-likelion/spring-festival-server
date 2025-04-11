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

    private String host;

    private Long likeCount;

    private Integer enterNum;

    private Integer waitingNum;

    @OneToMany(mappedBy = "pub", cascade = CascadeType.REMOVE)
    private List<Waiting> waitingList;
}
