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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    //여러개의 Order를 한명의 Member가 생성할 수 있음, Many = Order, One = Member
    //@JoinColumn 의 name이 FK의 이름이 됨.
    //Order의 member가 FK와 가깝기 때문에 Order가 주인임.

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    /*
     * OneToOne 관계의 경우 어느쪽에 FK를 둬도 상관은 없음, 보통 액세스를 많이 하는쪽에 FK를 지정함.
     * 보통의 경우 Order -> Delivery 접근 방식이 많기 때문에 Order에 FK를 지정하고 관계의 주인으로 지정함.
     * */

    private LocalDateTime orderDate; //주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //enum type, 주문 상태 [ORDER, CANCEL]

    //연관관계 메서드(양방향 연관관계)
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        //OrderItem... orderItems -> Java5의 가변 인자(Varargs), 여러개의 매개변수를 넘겨받을 수 있음. 가변 인자는 항상 마지막 매개변수로만 사용 가능.

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비즈니스 로직
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();

//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;
    }
}
