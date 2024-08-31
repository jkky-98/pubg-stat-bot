package hello.pubg_stat.service.discord.message;

import hello.pubg_stat.domain.Member;
import hello.pubg_stat.repository.MemberRepository;
import hello.pubg_stat.repository.dto.AllStatDto;
import hello.pubg_stat.repository.mybatis.mapper.MemberStatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MemberStatMapper memberStatMapper;
    private final MemberRepository memberRepository;

    @Transactional
    public List<Map<String, Object>> getDealt(String discordId) {
        Optional<Member> byDiscordId = memberRepository.findByDiscordId(discordId);
        if (byDiscordId.isPresent()) {
            Member member = byDiscordId.get();
            String userName = member.getUserName();
            List<Map<String, Object>> averageDealtByMapName = memberStatMapper.getAverageDealtByMapName(userName);
            log.info("요청 결과 : {}", averageDealtByMapName);
            return averageDealtByMapName;
        } else {
            // null
            return new ArrayList<>();
        }
    }

    @Transactional
    public List<AllStatDto> getAllStat() {
        return memberStatMapper.getAllStat();
    }
}
