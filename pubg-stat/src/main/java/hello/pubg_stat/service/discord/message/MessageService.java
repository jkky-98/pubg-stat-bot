package hello.pubg_stat.service.discord.message;

import hello.pubg_stat.repository.mybatis.mapper.MemberStatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MemberStatMapper memberStatMapper;

    public List<Map<String, Object>> getDealt(String userName) {
        //Todo: DiscordNameResolver로 이름 찾기 기능 필요
        String name = "meang9_-"; // 테스트용

        List<Map<String, Object>> averageDealtByMapName = memberStatMapper.getAverageDealtByMapName(name);
        log.info("요청 결과 : {}", averageDealtByMapName);

        return averageDealtByMapName;
    }

}
