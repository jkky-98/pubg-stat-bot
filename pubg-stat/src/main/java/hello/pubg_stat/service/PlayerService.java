package hello.pubg_stat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.pubg_stat.domain.Member;
import hello.pubg_stat.repository.MatchRepository;
import hello.pubg_stat.repository.MemberRepository;
import hello.pubg_stat.service.pubgapi.PubgApiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PubgApiParser pubgApiParser;
//    private final MatchRepository matchRepository;
//    private final MatchService matchService;
    private final MemberRepository memberRepository;

    @Transactional
    public Member registPlayer(String platform, String playerName) {
        String accountId = getAccountId(platform, playerName);
        Member member = new Member(playerName, accountId, platform);
        Member savedMember = memberRepository.save(member);

        return savedMember;
    }

    @Transactional
    public Member registMatchMetaPlayerRegisted() {
        List<Member> memberAll = memberRepository.findMemberAll();
        for (Member member : memberAll) {
            List<String> matchIds = getMatchIds(member.getPlatform(), member.getUserName());
            for (String matchId : matchIds) {
//                matchService.saveMatch(member.getPlatform(), matchId);
            }
        }
        return null;
    }

    public ResponseEntity<String> getSeasonStatPlayer(String platform, String accountId) {
        return pubgApiParser.getSeasonStatPlayerResponseEntity(platform, accountId);
    }

    public String getAccountId(String platform, String playerName) {

        ResponseEntity<String> response = pubgApiParser.getStringResponseEntity(platform, playerName);

        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode dataNode = rootNode.path("data").get(0);
            JsonNode idNode = dataNode.path("id");

            log.info("조회된 Account ID : {}", idNode.asText());

            return idNode.asText();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

    }

    public List<String> getMatchIds(String platform, String playerName) {

        ResponseEntity<String> response = pubgApiParser.getStringResponseEntity(platform, playerName);

        String responseBody = response.getBody();

        // matchId를 담을 리스트
        List<String> matchIds = new ArrayList<>();

        try {
            // ObjectMapper를 사용하여 JSON 문자열을 JsonNode로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // "data" 배열에 접근
            JsonNode dataNode = rootNode.path("data").get(0);
            JsonNode relationshipsNode = dataNode.path("relationships");
            JsonNode matchesNode = relationshipsNode.path("matches");
            JsonNode matchDataArray = matchesNode.path("data");

            // matchDataArray를 순회하여 matchId를 추출
            Iterator<JsonNode> elements = matchDataArray.elements();
            while (elements.hasNext()) {
                JsonNode matchNode = elements.next();
                String matchId = matchNode.path("id").asText();
                matchIds.add(matchId);
            }

        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

        return matchIds;
    }
}
