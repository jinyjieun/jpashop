package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //@NoArgsConstructor(access = AccessLevel.PROTECTED)
        // -> createOrderItem 외의 방법으로 생성하는 것을 막기 위해 생성자를 protected 로 지정해주는 어노테이션

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        //배송정보와 주문상품은 별도의 persist 를 하지 않고 주문만 persist 하는 이유, CascadeType 을 ALL 로 지정해두었기 때문.
        //CascadeType.ALL 로 지정해놓으면 별도로 persist 할 필요가 없음 -> 때문에 조심해서 사용해야 함.
        //CascadeType.ALL 로 사용하는 경우 -> 1. persist lifecycle 이 동일할 때, 2. 참조하는 주인이 private owner 일때
        //* private owner : User(사용자 기본 정보) -> UserDetail(사용자 상세정보, 다른 곳에서 참조하지 않음) 와 같이 다른 곳에서 참조할 가능성이 없을 때
        //위와 같은 경우에만 CascadeType.ALL 를 사용하는 것이 좋음, 때에 따라 굉장히 위험해질 수 있음. 미래까지 고려해야 함.
        //지금도, 미래에도 다른 곳에서 참조할 가능성이 없어야만 private owner 라고 볼 수 있음.

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
    }

}
