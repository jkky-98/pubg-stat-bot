package hello.pubg_stat.service.pubgapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//
@Service
@Slf4j
public class PubgApiParser {

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public PubgApiParser(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<String> getStringResponseEntity(String platform, String playerName) {
        // API URL을 동적으로 생성
        String url = String.format("%s/shards/%s/players?filter[playerNames]=%s", apiUrl, platform, playerName);
        // HttpEntity 객체 생성 (헤더 포함)
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        // GET 요청을 보내고 응답 받기
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> getMatchResponseEntity(String platform, String matchId) {
        String url = String.format("%s/shards/%s/matches/%s", apiUrl, platform, matchId);
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> getSeasonStatPlayerResponseEntity(String platform, String accountId) {
        String url = String.format("%s/shards/%s/players/%s/seasons/lifetime", apiUrl, platform, accountId);
        HttpEntity<String> entity = new HttpEntity<>(getHttpHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Accept", "application/vnd.api+json");
        return headers;
    }
}
