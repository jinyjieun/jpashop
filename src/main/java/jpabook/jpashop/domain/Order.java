package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    //DB의 order by 예약어 때문에 order라는 이름은 대부분 사용할 수 없음.
    //@Table(name = "orders") 로 Table 이름을 명시해주면 orders 라는 테이블이 생성됨.

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    //여러개의 Order를 한명의 Member가 생성할 수 있음, Many = Order, One = Member
    //@JoinColumn 의 name이 FK의 이름이 됨.
    //Order의 member가 FK와 가깝기 때문에 Order가 주인임.

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    private Delivery delivery;

    private LocalDateTime orderDate; //주문 시간

    private OrderStatus status; //enum type, 주문 상태 [ORDER, CANCEL]
}
