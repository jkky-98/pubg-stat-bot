package hello.pubg_stat.repository.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class MatchParticipantDto {

    private JsonNode participantNode;
    private String roster;
}
