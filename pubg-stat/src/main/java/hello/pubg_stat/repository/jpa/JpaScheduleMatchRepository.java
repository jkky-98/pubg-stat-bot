package hello.pubg_stat.repository.jpa;

import hello.pubg_stat.domain.MatchSchedule;
import hello.pubg_stat.domain.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaScheduleMatchRepository extends JpaRepository<MatchSchedule, String> {
}
