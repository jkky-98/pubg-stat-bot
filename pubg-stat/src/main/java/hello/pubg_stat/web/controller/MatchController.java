package hello.pubg_stat.web.controller;

import hello.pubg_stat.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MatchController {

    private final MatchService matchService;

    @GetMapping("match-detail/{platform}/{matchId}")
    public ResponseEntity<String> getMatchInfo(
            @PathVariable String matchId,
            @PathVariable String platform
    ) {
        return matchService.getMatchResponseEntity(platform, matchId);
    }

    @PostMapping("match-meta-save/{platform}/{matchId}")
    public String saveMatchInfo(
            @PathVariable String platform,
            @PathVariable String matchId
    ) {
        return null;
    }
}
