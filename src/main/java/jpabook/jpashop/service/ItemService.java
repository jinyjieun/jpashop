package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        //1. 변경감지의 경우, 영속성 컨텍스트 안에서 영속 엔티티를 조회한 후에
        Item findItem = itemRepository.findOne(itemId);

        //데이터를 수정하여 트랜잭션 커밋 시점에 변경 감지가 동작할 수 있도록 한다.
        //데이터를 변경할 경우에는 setter 를 사용하지 말고 아래와 같이 별도의 비즈니스 로직을 추가하는게 좋음.
        //해당 메서드가 구현되는 로직 자체는 merge 와 동일하다. (but, 차이점이 있음. -> README 에 자세히 기록)
        findItem.change(name, price, stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
