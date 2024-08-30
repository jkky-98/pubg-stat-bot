package hello.pubg_stat.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Match {
    @Id
    private String matchId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "game_mode")
    private String gameMode;

    @Column(name = "mapName")
    private String mapName;

    private Integer duration;

    @Column(name = "match_type")
    private String matchType;

    public Match() {
    }

    public Match(String matchId, LocalDateTime createdAt, String gameMode, String mapName, Integer duration, String matchType) {
        this.matchId = matchId;
        this.createdAt = createdAt;
        this.gameMode = gameMode;
        this.mapName = mapName;
        this.duration = duration;
        this.matchType = matchType;
    }
}
