package hello.pubg_stat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.pubg_stat.domain.Match;
import hello.pubg_stat.domain.Member;
import hello.pubg_stat.domain.Participant;
import hello.pubg_stat.domain.Stat;
import hello.pubg_stat.repository.MemberRepository;
import hello.pubg_stat.repository.jpa.JpaMatchRepository;
import hello.pubg_stat.repository.jpa.JpaParticipantRepository;
import hello.pubg_stat.repository.jpa.JpaStatRepository;
import hello.pubg_stat.service.pubgapi.PubgApiParser;
import hello.pubg_stat.util.MapNameResolver;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {

    private final PubgApiParser pubgApiParser;
    private final MapNameResolver mapNameResolver;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MemberRepository memberRepository;

    private final JpaMatchRepository matchRepository;
    private final JpaStatRepository statRepository;
    private final JpaParticipantRepository participantRepository;


    // 임시용
    public ResponseEntity<String> getMatchResponseEntity(String platform, String matchId) {
        return pubgApiParser.getMatchResponseEntity(platform, matchId);
    }

    // 임시용
    public String getMatchString(ResponseEntity<String> matchResponseEntity) {
        return matchResponseEntity.getBody();
    }

    private String getMatchBody(String platform, String matchId) {
        ResponseEntity<String> matchResponseEntity = pubgApiParser.getMatchResponseEntity(platform, matchId);
        return matchResponseEntity.getBody();
    }

    public Match saveMatch(String platform, String matchId) {
        String matchBody = getMatchBody(platform, matchId);

        try {
            // 초기화

            // 정보 분석
            JsonNode rootNode = objectMapper.readTree(matchBody);
            JsonNode dataNode = rootNode.path("data");
            JsonNode metaNode = dataNode.path("attributes");
            JsonNode includedNode = rootNode.path("included");
            if (metaNode.path("gameMode").asText().equals("squad")
                    || metaNode.path("gameMode").asText().equals("duo")
                    && metaNode.path("matchType").asText().equals("official"))
            {
                // 객체 생성
                Match match = matchMetaBuilder(matchId, metaNode);
                log.info("MATCH 데이터 저장 객체 : {} ", match);
                // 저장
                matchRepository.save(match);

                List<Member> memberAll = memberRepository.findMemberAll();
                List<String> playerIdsToFind = new ArrayList<>();

                for (Member member : memberAll) {
                    playerIdsToFind.add(member.getAccountId());
                }

                log.info("등록 사용자 : {}", playerIdsToFind);

                for (JsonNode participantRosterNode : includedNode) {
                    JsonNode statsNode = participantRosterNode.path("attributes").path("stats");
                    String playerId = statsNode.path("playerId").asText();

                    if (playerIdsToFind.contains(playerId)) {
                        String participantId = participantRosterNode.path("id").asText();

                        Map<String, RosterInfoDto> rosterInfo = getRosterInfo(includedNode, participantId);

                        JsonNode statNode = participantRosterNode.path("attributes").path("stats");

                        String name = statNode.path("name").asText();
                        Integer teamMateCount = rosterInfo.get(participantId).numOfTeamMate;
                        Boolean firstDeath = getFirstDeath(participantId, rosterInfo.get(participantId).teamMateIds, includedNode);
                        Boolean teamWin = false;
                        if (statNode.path("winPlace").asInt() == 1) {
                            teamWin = true;
                        }
                        Boolean teamTopTen = false;
                        if (statNode.path("winPlace").asInt() <= 10) {
                            teamTopTen = true;
                        }
                        Integer dbnos = statNode.path("DBNOs").asInt();
                        Integer dealt = statNode.path("damageDealt").asInt();
                        Integer kills = statNode.path("kills").asInt();
                        Integer assists = statNode.path("assists").asInt();
                        Integer boosts = statNode.path("boosts").asInt();
                        Integer timeSurvived = statNode.path("timeSurvived").asInt();
                        Integer revives = statNode.path("revives").asInt();

                        Stat stat = new Stat();
                        stat.setParticipantId(participantId);
                        stat.setPlayerId(playerId);
                        stat.setName(name);
                        stat.setTeamMateCount(teamMateCount);
                        stat.setFirstDeath(firstDeath);
                        stat.setTeamWin(teamWin);
                        stat.setTeamTopTen(teamTopTen);
                        stat.setDbnos(dbnos);
                        stat.setDealt(dealt);
                        stat.setKills(kills);
                        stat.setAssists(assists);
                        stat.setBoosts(boosts);
                        stat.setTimeSurvived(timeSurvived);
                        stat.setRevives(revives);

                        log.info("STAT 데이터 : {}", stat);

                        Participant participant = new Participant();
                        participant.setMatchId(dataNode.path("id").asText());
                        participant.setParticipantId(participantId);
                        participant.setRosterId(rosterInfo.get(participantId).getRosterId());
                        participant.setPlayerId(playerId);

                        log.info("Player 데이터 : {}", participant);

                        participantRepository.save(participant);
                        statRepository.save(stat);
                    }
                }
                return match;
            } else {
                return new Match();
            }

        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

    }

    private Boolean getFirstDeath(String participantIdToCheck, List<String> teamMateIds, JsonNode includedNode) {
        // 결과를 저장할 Map
        Map<String, Integer> participantTimeSurvivedMap = new HashMap<>();

        // participants 배열을 순회하여 ID와 timeSurvived를 추출
        for (JsonNode canParticipantNode : includedNode) {
            if ("participant".equals(canParticipantNode.path("type").asText())) {
                String id = canParticipantNode.path("id").asText();
                if (teamMateIds.contains(id)) {
                    participantTimeSurvivedMap.put(id, canParticipantNode.path("attributes").path("stats").path("timeSurvived").asInt());
                }
            }
        }

        Integer participantTimeSurvived = participantTimeSurvivedMap.get(participantIdToCheck);

        // 가장 작은 timeSurvived 값을 찾기
        boolean isSmallest = true;

        for (Map.Entry<String, Integer> entry : participantTimeSurvivedMap.entrySet()) {
            if (entry.getKey().equals(participantIdToCheck)) {
                continue;
            }
            if (entry.getValue() < participantTimeSurvived) {
                isSmallest = false;
                break;
            }
        }

        return isSmallest;
    }

    private Map<String, RosterInfoDto> getRosterInfo(JsonNode includedNode, String participantId) {
        // 결과 저장할 Set
        Set<String> rosterIdsWithParticipants = new HashSet<>();


        for (JsonNode canRosterNode : includedNode) {
            if ("roster".equals(canRosterNode.path("type").asText())) {

                // participants 배열 추출
                ArrayNode participantsArray = (ArrayNode) canRosterNode.path("relationships").path("participants").path("data");

                Set<String> rosterParticipantIds = new HashSet<>();
                for (JsonNode participantNode : participantsArray) {
                    rosterParticipantIds.add(participantNode.path("id").asText());
                }

                if (rosterParticipantIds.contains(participantId)) {
                    String rosterId = canRosterNode.path("id").asText();
                    Integer numOfTeamMate = rosterParticipantIds.size();
                    List<String> teamMateIds = new ArrayList<>();

                    // participants 배열을 순회하여 ID를 추출
                    for (JsonNode participantNode : participantsArray) {
                        String participantIdInRoster = participantNode.path("id").asText();
                        teamMateIds.add(participantIdInRoster);
                    }

                    // teamMate들의 죽음 시간 조사

                    return Map.of(participantId, new RosterInfoDto(rosterId, numOfTeamMate, teamMateIds));
                }

            }
        }
        return Map.of(participantId, new RosterInfoDto());

    }

    @Data
    private class RosterInfoDto {
        private String rosterId;
        private Integer numOfTeamMate;
        private List<String> teamMateIds;

        public RosterInfoDto(String rosterId, Integer numOfTeamMate, List<String> teamMateIds) {
            this.rosterId = rosterId;
            this.numOfTeamMate = numOfTeamMate;
            this.teamMateIds = teamMateIds;
        }

        public RosterInfoDto() {
        }
    }

    private Match matchMetaBuilder(String matchId, JsonNode metaNode) {
        Instant createdAtInstant = Instant.parse(metaNode.path("createdAt").asText());
        String mapName = mapNameResolver.process(metaNode.path("mapName").asText());
        LocalDateTime createAt = LocalDateTime.ofInstant(createdAtInstant, ZoneId.systemDefault());
        Integer duration = metaNode.path("duration").asInt();
        String gameMode = metaNode.path("gameMode").asText();
        String matchType = metaNode.path("matchType").asText();

        return new Match(matchId, createAt, gameMode, mapName, duration, matchType);
    }

    /**
     *
     * @param rootNode
     * @param type -> "participant" or "roster"
     * @return
     */
    private Optional<JsonNode> findRosterNode(JsonNode rootNode, String type) {
        if (rootNode.has("included")) {
            ArrayNode includedArray = (ArrayNode) rootNode.path("included");
            for (JsonNode node : includedArray) {
                if (type.equals(node.path("type").asText())) {
                    return Optional.of(node);
                }
            }
        }
        return Optional.empty();
    }

    private void findParticipantId(Member member, JsonNode includedNode) {

    }
}
