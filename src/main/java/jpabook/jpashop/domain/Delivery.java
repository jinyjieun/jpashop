package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;
    //관계의 주인이 Order이기 때문에 delivery에 mappedBy를 설정해줌.

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //배송 상태 [READY, COMP]
    //ENUM TYPE은 항상 @Enumerated 어노테이션을 지정해줘야함. 지정하지 않을 경우 ORDINAL이 Default
    //EnumType은 (EnumType.ORDINAL), (EnumType.STRING) 두가지가 있음.
    //ORDINAL - enum value를 1, 2, 3, .... sequentiall 하게 지정한다. 중간에 enum value가 추가될 경우, 순서가 맞지 않아 장애가 발생할 수 있으므로 운영 환경에 적합하지 않음.
    //STRING - enum value를 string 그대로 지정한다.
    //ORDINAL을 쓰지말고 STRING을 써야함.
}
