package hello.pubg_stat.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.pubg_stat.domain.Member;
import hello.pubg_stat.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("account-id/{platform}/{playerName}")
    public String getPlayerAccountId(
            @PathVariable String platform,
            @PathVariable String playerName) {
        return playerService.getAccountId(platform, playerName);
    }

    @GetMapping("match-id-for-14/{platform}/{playerName}")
    public List<String> getPlayerMatchIds(
            @PathVariable String platform,
            @PathVariable String playerName
    ) {
        return playerService.getMatchIds(platform, playerName);
    }

    @GetMapping("/regist/{platform}/{playerName}")
    public Member registPlayer(
            @PathVariable String platform,
            @PathVariable String playerName
    ) {
        return playerService.registPlayer(platform, playerName);
    }

    @GetMapping("/season/{platform}/{playerName}")
    public ResponseEntity<String> getSeasonStatPlayer(
            @PathVariable String platform,
            @PathVariable String playerName
    ) {
        String accountId = playerService.getAccountId(platform, playerName);
        return playerService.getSeasonStatPlayer(platform, accountId);
    }
}
