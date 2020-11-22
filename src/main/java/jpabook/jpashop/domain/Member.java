package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    /*
     * 기본키 자동 생성 방법
     * ex) @GeneratedValue(strategy = GenerationType.IDENTITY) 와 같은 방식으로 표기한다.
     *
     * 종류)
     * IDENTITY - 기본 키 생성을 데이터베이스에 위임함(데이터베이스 의존적)
     *            사용 DB) MySQL, PostgreSQL, SQL Server, DB2
     * SEQUENCE - 데이터베이스 시퀀스를 사용해서 기본키를 할당함.(데이터베이스 의존적)
     *            @SequenceGenerator를 사용하여 시퀀스 생성기를 등록하고, 실제 데이터베이스의 생성될 시퀀스이름을 지정해줘야 함.
     *            사용 DB) 시퀀스를 지원하는 Oracle, PostgreSQL, DB2, H2
     * TABLE - 키 생성 테이블을 사용함.
     *         키 생성 테이블을 하나 만들고 이름과 값으로 사용할 컬럼을 만드는 방법, 테이블을 사용하기 때문에 데이터베이스 벤더에 상관없이 모든 데이터베이스 적용이 가능함.
     *
     * AUTO - 데이터베이스 벤더에 의존하지 않고 데이터베이스는 기본키를 할당함.
     *        데이터베이스에 따라서 위 3가지 방법 중 하나를 자동으로 할당해줌
     *        Oracle일 경우 SEQUENCE 방식을 할당, 데이터베이스가 변경되어도 코드를 수정할 필요가 없음.
     *
     * 기본 키를 직접 할당하기 위해서는 @Id 어노테이션만 사용하면 되고, 자동 생성 전략을 사용하기 위해서는 @Id에 @GeneratedValue를 추가하고 원하는 키 생성 전략을 선택하면 됨.
     * */

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;
    //@Embedded 내장 Type이라는것을 명시해주기 위해 사용하는 어노테이션
    //@Embedded, @Embeddable 둘중에 하나만 있어도 되지만 Embedded Type이라는것을 명시해주기 위해서 양쪽에 다 쓰는것을 선호함.

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    //여러개의 Order를 한명의 Member가 생성할 수 있음, Many = Order, One = Member
    //Member와 Order의 경우, 양방향 연관관계이기 때문에 양쪽에서 다 값을 바꿀 수 있으면 문제가 생김. 관계의 주인을 지정해야함.
    //FK는 하나이기 때문에 관계의 주인을 지정하고 한쪽에서만 수정할 수 있게 해야함.
    //FK와 가까운 곳을 주인으로 지정하면 됨.
    //주인이 아닌 쪽에 MappedBy를 지정해줌. (mappedBy = "member") Order Table에 있는 member 필드를 통해서 매핑된거라는 의미
}
