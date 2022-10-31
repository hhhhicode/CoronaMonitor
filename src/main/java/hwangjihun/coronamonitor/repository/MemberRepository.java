package hwangjihun.coronamonitor.repository;

import hwangjihun.coronamonitor.domain.Member;
import hwangjihun.coronamonitor.domain.MemberUpdateDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    void update(Long id, MemberUpdateDto memberUpdateDto);
    Optional<Member> findById(Long id);
    Optional<Member> findByUserId(String userId);

    List<Member> findAll();
}
