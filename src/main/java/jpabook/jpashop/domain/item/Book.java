package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item {
    //@DiscriminatorValue("B") dtype값을 B로 지정, 어노테이션을 별도 지정하지 않으면 Class Name이 그대로 들어감.
    private String author;
    private String isbn;
}
