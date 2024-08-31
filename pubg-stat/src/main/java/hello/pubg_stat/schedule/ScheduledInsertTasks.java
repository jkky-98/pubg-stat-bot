package hello.pubg_stat.schedule;

import hello.pubg_stat.schedule.service.MatchForSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledInsertTasks {

    private final MatchForSchedule matchForSchedule;

    @Scheduled(fixedRate = 10000)
    public void apiInsertTasks() {
        matchForSchedule.analysisMatchSchedule(); 
    }
}
