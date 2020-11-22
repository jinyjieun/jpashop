package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        //결과는 True가 나와야 함.
        //같은 트랜잭션 안에서 저장하고 조회할 경우 영속성 컨텍스트가 똑같음.
        //같은 영속성 컨텍스트 안에서는 id값이(식별자) 같을 경우 같은 Entity로 식별함.
        //해당 Test Code에서는 Insert Query만 수행하고 Select Query는 수행하지조차 않음.
        //같은 영속성 컨텍스트에 Entity가 존재하기 때문에 새로 select 하지 않고 1차 캐시에서 꺼내옴.
        Assertions.assertThat(findMember).isEqualTo(member);
    }
}
/*
* 스프링 부트 버전을 2.2.X로 지정했기 때문에 JUNIT 5 버전을 사용해야 함.
* JUNIT 5 버전을 사용하면 기본 설정으로는 안되고 따로 설정을 변경해줘야만 정상적으로 TestUnit을 사용할 수 있음.
* (기본 설정으로 Test를 진행할 경우 > No tests found for given includes: 어쩌구 하면서 에러나면서 테스트 실행이 안됨.)
* Settings > Gradle > Run Tests Using > Gradle로 기본 설정되어있는것을 IntelliJ IDEA로 변경
* 변경 후 실행하면 정상적으로 실행됨.
*
* 그리고 처음 Test를 실행하면 오류남.
* InvalidDataAccessApiUsageException: No EntityManager with actual transaction available for current thread
* EntityManager를 통한 모든 데이터 변경은 항상 트랜잭션 안에서 수행되어야 함.
*
* @Transactional 옵션은 Spring F/W, Javax 모두에서 제공하고 있음.
* Spring F/W 에서 제공하는 트랜잭션 어노테이션을 사용할 것, 왜냐면 이미 스프링프레임워크를 쓰면서 스프링 종속적으로 설계를 하고있기 때문에 스프링꺼를 쓰는것을 권장함
* 또 스프링프레임워크에서 제공하는 트랜잭션에 쓸 수 있는 옵션이 많음.
*
* 스프링에서는 @Transactional 어노테이션이 Test Case에 있으면, Test Case를 모두 수행하고난 뒤에 모든 변경 사항을 바로 RollBack 해버림.
* RollBack 하기를 원하지 않을 경우, @Rollback(false) 어노테이션을 추가해주면 됨.
* */