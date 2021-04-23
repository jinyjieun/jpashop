package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //최신 버전 스프링에서는 생성자가 하나만 있는 경우, @Autowired 어노테이션이 없더라도 스프링이 자동으로 Injection 해줌.
    //@RequiredArgsConstructor - lombok이 memberRepository 필드 정보를 가지고 생성자를 자동으로 만들어주는것은 동일하나, final 필드에 한해서만 만든다.
    //@AllArgsConstructor - lombok이 memberRepository 필드 정보를 가지고 생성자를 자동으로 만들어줌.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     *
     * JPA의 모든 데이터 변경이나 로직들은 모두 트랜잭션 안에서 실행되어야 함. 조회만 할 경우는 readOnly = true 를 사용하면 성능을 좀 더 최적화 함.
     * 아래와 같이 method에 어노테이션을 별도 명시해줄 경우 별도로 명시해준것이 적용됨.(최상단은 readOnly true 일지라도.. @Transactional이 적용됨.)
     */
    @Transactional
    public Long join(Member member) {

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {

        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
