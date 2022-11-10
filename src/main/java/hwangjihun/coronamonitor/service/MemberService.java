package hwangjihun.coronamonitor.service;

import hwangjihun.coronamonitor.domain.members.Member;
import hwangjihun.coronamonitor.domain.members.MemberUpdateDto;
import hwangjihun.coronamonitor.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public void update(Long id, MemberUpdateDto memberUpdateDto) {
        memberRepository.update(id, memberUpdateDto);
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Member login(String loginId, String password) {
        return memberRepository.findByUserId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
