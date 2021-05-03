package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            //신규 등록 Case
            em.persist(item);
        } else {
            //Update 같은 Case
            //영속성 컨텍스트 내에서 영속 Entity 를 조회한 뒤에 조회한 영속 엔티티의 '모든 값'을 변경 엔티티의 값으로 변경한다.
            //변경한 뒤에 아래와 같이 영속 상태인 엔티티를 반환한다.
            //Item merge = em.merge(item); -> item 은 준영속, merge 는 영속 상태의 엔티티이다.
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
