package hwangjihun.coronamonitor.repository;

import hwangjihun.coronamonitor.domain.Member;
import hwangjihun.coronamonitor.domain.MemberUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class JpaMemberRepository implements MemberRepository{

    private final EntityManager entityManager;

    public JpaMemberRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Member save(Member member) {
        entityManager.persist(member);
        return member;
    }

    @Override
    public void update(Long id, MemberUpdateDto memberUpdateDto) {
        Member findMember = entityManager.find(Member.class, id);
        findMember.setPassword(memberUpdateDto.getPassword());
        findMember.setUserName(memberUpdateDto.getUserName());
        findMember.setAge(memberUpdateDto.getAge());
        // JPA가 변화를 감지하고 자동으로 update 해준다.
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member findMember = entityManager.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    @Override
    public Optional<Member> findByUserId(String userId) {
        return findAll().stream()
                .filter(m -> m.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        String jpql = "select m from Member m";

        TypedQuery<Member> query = entityManager.createQuery(jpql, Member.class);

        return query.getResultList();
    }
}
