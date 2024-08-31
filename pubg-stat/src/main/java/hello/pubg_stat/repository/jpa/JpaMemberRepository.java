package hello.pubg_stat.repository.jpa;

import hello.pubg_stat.domain.Member;
import hello.pubg_stat.repository.MemberRepository;
import hello.pubg_stat.repository.UpdateMemberDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public void update(UpdateMemberDto updateMemberDto) {
        Member member = em.find(Member.class, updateMemberDto.getId());

        member.setUserName(updateMemberDto.getUserName());
        member.setPlatform(updateMemberDto.getPlatform());
        member.setAccountId(updateMemberDto.getAccountId());
    }

    @Override
    public Optional<Member> findByAccountId(String accountId) {
        String jpql = "SELECT m FROM Member m WHERE m.accountId = :accountId";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class);
        query.setParameter("accountId", accountId);
        Member member = query.getResultStream().findFirst().orElse(null);

        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByDiscordId(String discordId) {
        String jpql = "SELECT m FROM Member m WHERE m.discordId = :discordId";

        TypedQuery<Member> query = em.createQuery(jpql, Member.class);
        query.setParameter("discordId", discordId);
        Member member = query.getResultStream().findFirst().orElse(null);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findMemberAll() {
        // JPQL 쿼리 작성: 모든 Member 엔티티를 선택
        String jpql = "SELECT m FROM Member m";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class);
        List<Member> members = query.getResultList();
        log.info("Retrieved members: {}", members);

        return members;
    }
}
