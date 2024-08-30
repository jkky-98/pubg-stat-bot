package hello.pubg_stat.repository;

import jakarta.persistence.*;

import java.time.LocalDateTime;

public class UpdateMatchDto {
    @Id
    private String id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "game_mode")
    private String gameMode;

    @Column(name = "mapName")
    private String mapName;

    private String duration;

    @Column(name = "match_type")
    private String matchType;

    @Column(name = "isProcess")
    private Boolean isProcess;
}
