package likelion.festival.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class LikeRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ip;

    private Boolean redLight;

    private LocalDateTime sendTime;

    public LikeRequestLog(String ip, Boolean redLight, LocalDateTime sendTime) {
        this.ip = ip;
        this.redLight = redLight;
        this.sendTime = sendTime;
    }

    public void setRedLight(Boolean redLight) {
        this.redLight = redLight;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }
}
