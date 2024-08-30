package hello.pubg_stat.repository.jpa;

import hello.pubg_stat.domain.Match;
import hello.pubg_stat.domain.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaStatRepository extends JpaRepository<Stat, String> {
}
