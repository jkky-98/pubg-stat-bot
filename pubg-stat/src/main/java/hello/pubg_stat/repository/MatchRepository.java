package hello.pubg_stat.repository;

import hello.pubg_stat.domain.Match;
import hello.pubg_stat.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    Member save(Match match);

    void update(UpdateMatchDto updateMatchDto);

    Optional<Match> findById(String Id);

    List<Match> findMemberAll();
}
