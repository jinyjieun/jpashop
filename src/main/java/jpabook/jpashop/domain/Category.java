package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
                joinColumns = @JoinColumn(name = "category_id"),
                inverseJoinColumns = @JoinColumn(name="item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();
}

/*
 * FK 를 꼭 지정해야하는지? 시스템마다, 상황마다 다르다.
 * - 시스템의 실시간 트래픽이 굉장히 중요하고 정합성보다는 성능 등 유연하게 잘 서비스 되는것이
 * 더 중요한 시스템의 경우, FK를 지정하지 않고 Index만 잘 구성해주면 됨.
 * - 돈과 관련된 너무 중요한 데이터이기 때문에 정합성이 잘 맞아야한다 이런 이유가 있다면
 * FK 지정에 대해 진지하게 고민해볼 필요가 있음.
 */
