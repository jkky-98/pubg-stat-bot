package hello.pubg_stat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Stat {

    @Id
    private String participantId;

    private String playerId;
    private String name;
    private Integer teamMateCount;
    private Boolean firstDeath;
    private Boolean teamWin;
    private Boolean teamTopTen;
    private Integer dbnos;
    private Integer dealt;
    private Integer kills;
    private Integer assists;
    private Integer boosts;
    private Integer timeSurvived;
    private Integer revives;

    public Stat() {
    }
}
