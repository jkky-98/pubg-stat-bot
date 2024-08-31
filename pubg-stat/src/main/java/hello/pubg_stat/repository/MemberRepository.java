package hello.pubg_stat.repository;

import hello.pubg_stat.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    void update(UpdateMemberDto updateMemberDto);

    Optional<Member> findByAccountId(String accountId);

    List<Member> findMemberAll();

    Optional<Member> findByDiscordId(String discordId);
}
