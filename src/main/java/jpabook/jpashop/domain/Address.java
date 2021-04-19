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

    //JAP 스펙 상 엔티티나 임베디드 타입(@Embeddable)은
    //자바 기본 생성자(Default Constructor)를 public 또는 protected 로 설정해야한다.
    //이런 제약을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용할 수 있도록 지원해야 하기 때문.
    //리플렉션은 구체적인 클래스 타입을 알지 못해도, 그 클래스의 메소드, 타입, 변수들에 접근할 수 있도록 해주는 자바 API
    //JPA의 기본 스펙은 엔티티에 기본 생성자가 필수로 있어야 한다. 하지만 하이버네이트 같은 구현체들은 조금 더 유연하게 바이트 코드를
    //조작하는 라이브러리등을 통해서 이런 문제를 회피한다. 완벽한 해결책은 아니므로 상황에 따라 다르다.
    //기본 가이드를 따라 기본 생성자를 넣어주어야 한다.
    protected Address() {

    }

    //값 타입 같은 경우에는 변경이 불가능하도록 설계해야함.
    //@Setter 를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스 만들기.
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
