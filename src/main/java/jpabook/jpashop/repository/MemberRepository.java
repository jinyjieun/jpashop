package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    /*
    * Repository는 DAO와 비슷한 개념이라고 보면 됨.
    * @Repository 어노테이션은 스프링이 제공하는 기본타입, 컴포넌트 스캔의 대상이 되는 어노테이션
    * 컴포넌트 스캔의 대상이 되서 자동으로 스프링 빈에 등록이 됨.
    *
    * 스프링 부트 + 스프링 데이터 JPA를 쓰면 @PersistenceContext -> @Autowired 로 바꿀 수 있음.
    * 스프링 데이터 JPA가 지원함.(스프링 데이터 JPA를 사용하지 않으면 아래와 같이 사용할 수 없음. 원래 엔티티 매니저는 @PersistenceContext 표준 어노테이션이 있어야만 인젝션됨)
    *
    * @PersistenceContext
    * private EntityManager em; -> 그래서 이런식으로 안써도 되고 아래와 같이 사용해도 됨. (아래와 같이 사용하려면 @RequiredArgsConstructor)
    * */

    private final EntityManager em;
    //엔티티 매니저 생성하는거는 부트가 알아서 해서 주입해줌.

    //커맨드와 쿼리를 분리해라.
    //아래는 사이드 이펙트를 일으키는 커맨드이기 때문에 저장을 하고 나면 가급적이면 리턴값을 만들지 않음.
    //persist 하면 영속성컨텍스트에 멤버 엔티티를 넣고 트랜잭션이 커밋되는 시점에 디비에 반영됨.
    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
