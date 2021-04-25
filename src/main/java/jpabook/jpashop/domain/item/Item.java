package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    /*
     * 상속 관계 매핑이기 때문에 전략을 지정해줘야함.
     * @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
     * JOINED - 가장 정규화된 스타일
     * SINGLE_TABLE - 한 테이블에 모든 정보를 다 넣음.
     * TABLE_PER_CLASS - CLASS 별로 각각 TABLE을 만듬.
     *
     * @DiscriminatorColumn(name = "dtype")
     * SINGLE_TABLE 전략을 사용하여 모든 CLASS 정보들을 한 테이블에 넣을거기 때문에 구분값으로 사용할 컬럼을 지정해준다.
     */

    @Id
    @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //비즈니스 로직
    /**
     * Stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * Stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
