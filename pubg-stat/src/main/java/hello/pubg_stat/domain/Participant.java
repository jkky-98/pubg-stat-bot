package hello.pubg_stat.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Participant {
    @Id
    private String participantId;
    private String matchId;
    private String rosterId;
    private String playerId;

    public Participant() {
    }
}
