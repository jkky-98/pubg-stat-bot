package hello.pubg_stat.repository.jpa;

import hello.pubg_stat.domain.Participant;
import hello.pubg_stat.domain.Stat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaParticipantRepository extends JpaRepository<Participant, String> {
}
