package hello.pubg_stat.repository.jpa;

import hello.pubg_stat.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMatchRepository extends JpaRepository<Match, String> {
}
