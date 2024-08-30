package hello.pubg_stat.service;

import hello.pubg_stat.domain.Match;
import hello.pubg_stat.domain.Member;
import hello.pubg_stat.domain.Participant;
import hello.pubg_stat.domain.Stat;
import hello.pubg_stat.repository.MemberRepository;
import hello.pubg_stat.repository.jpa.JpaParticipantRepository;
import hello.pubg_stat.repository.jpa.JpaStatRepository;
import hello.pubg_stat.service.pubgapi.PubgApiParser;
import hello.pubg_stat.util.MapNameResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
class MatchServiceTest {

    @Autowired
    MatchService matchService;

    @Autowired
    MapNameResolver mapNameResolver;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PubgApiParser pubgApiParser;

    @Autowired
    PlayerService playerService;

    @Autowired
    JpaStatRepository statRepository;

    @Autowired
    JpaParticipantRepository participantRepository;


    @Test
    @Transactional
    void matchSaveSuccess() {

        String platform = "steam";

        List<String> playerNames = List.of("meang9_-", "Eriri__-", "EmEmEmEm--");

        for (String playerName : playerNames) {
            String accountId = playerService.getAccountId(platform, playerName);
            Member member = new Member(playerName, accountId, platform);
            Member savedMember = memberRepository.save(member);
        }

        String matchId = "f8d57fb3-4ada-440a-b295-2e56fd385536";
        String createAtString = "2024-08-25T10:05:54Z";
        Instant createdAtInstant = Instant.parse(createAtString);
        LocalDateTime createAt = LocalDateTime.ofInstant(createdAtInstant, ZoneId.systemDefault());
        String mapNamePrev = "Baltic_Main";
        String mapName = mapNameResolver.process(mapNamePrev);
        String gameMode = "squad";
        int duration = 1537;
        String matchType = "official";

        Match match = matchService.saveMatch(platform, matchId);
        Match matchCompare = new Match(matchId, createAt, gameMode, mapName, duration, matchType);

        assertThat(match).isEqualTo(matchCompare);

        List<Stat> statAll = statRepository.findAll();
        List<Participant> parAll = participantRepository.findAll();

        assertThat(statAll.size()).isEqualTo(3);
        assertThat(parAll.size()).isEqualTo(3);

    }

}