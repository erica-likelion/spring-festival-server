package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class LostItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    private String name;

    private String description;

    private Boolean staffNotified;

    private String foundLocation;

    private String foundDate;

    private String foundTime;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = true)
//    private User user;

    public LostItem(
            String image,
            String name,
            String description,
            Boolean staffNotified,
            String foundLocation,
            String foundDate,
            String foundTime
    ) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.staffNotified = staffNotified;
        this.foundLocation = foundLocation;
        this.foundDate = foundDate;
        this.foundTime = foundTime;
    }
}

