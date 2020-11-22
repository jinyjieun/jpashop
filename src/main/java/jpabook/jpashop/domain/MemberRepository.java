package jpabook.jpashop.domain;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {
    /*
    * Repository는 DAO와 비슷한 개념이라고 보면 됨.
    * @Repository 어노테이션은 스프링이 제공하는 기본타입, 컴포넌트 스캔의 대상이 되는 어노테이션
    * 컴포넌트 스캔의 대상이 되서 자동으로 스프링 빈에 등록이 됨.
    * */

    @PersistenceContext
    private EntityManager em;
    //엔티티 매니저 생성하는거는 부트가 알아서 해줌

    //커맨드와 쿼리를 분리해라.
    //아래는 사이드 이펙트를 일으키는 커맨드이기 때문에 저장을 하고 나면 가급적이면 리턴값을 만들지 않음.
    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
