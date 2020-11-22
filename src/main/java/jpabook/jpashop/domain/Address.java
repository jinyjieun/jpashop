package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {
    //JPA의 내장 타입
    private String city;
    private String street;
    private String zipcode;
}
