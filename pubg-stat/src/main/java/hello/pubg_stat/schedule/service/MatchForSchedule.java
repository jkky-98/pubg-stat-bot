package hello.pubg_stat.schedule.service;

import hello.pubg_stat.domain.MatchSchedule;
import hello.pubg_stat.domain.Member;
import hello.pubg_stat.repository.MemberRepository;
import hello.pubg_stat.repository.jpa.JpaScheduleMatchRepository;
import hello.pubg_stat.service.MatchService;
import hello.pubg_stat.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchForSchedule {

    private final PlayerService playerService;
    private final MemberRepository memberRepository;
    private final JpaScheduleMatchRepository scheduleMatchRepository;
    private final MatchService matchService;

    private static final String platform = "steam";

    /**
     *  등록된 사용자 수 만큼의 API 조회 시도
     */
    @Transactional
    public void saveMatchSchedule() {
        List<Member> memberAll = memberRepository.findMemberAll();
        log.info("스케줄링 작업 대상 등록 멤버 : {}", memberAll);

        for (Member member : memberAll) {
            // API 요청 부분 //
            List<String> matchIds = playerService.getMatchIds(platform, member.getUserName());

            for (String matchId : matchIds) {

                if (!scheduleMatchRepository.existsById(matchId)) {
                    MatchSchedule matchSchedule = new MatchSchedule();

                    matchSchedule.setMatchId(matchId);
                    matchSchedule.setIsProcessed(false);

                    scheduleMatchRepository.save(matchSchedule);
                }
            }
        }

        log.info("스케줄링 작업(작업할 MATCH_ID) 저장 완료");
    }

    @Transactional
    public void analysisMatchSchedule() {
        List<MatchSchedule> scheduleMatchAll = scheduleMatchRepository.findAll();
        log.info("[분석 시작] 스케줄링 리스트 : {}", scheduleMatchAll);
        for (MatchSchedule matchSchedule : scheduleMatchAll) {
            if (!matchSchedule.getIsProcessed()) {
                log.info("분석을 시작합니다 : MatchId : {}", matchSchedule.getMatchId());
                matchService.saveMatch(platform, matchSchedule.getMatchId());
                matchSchedule.setIsProcessed(true);
                break;
            }
        }
    }
}
