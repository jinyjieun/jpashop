# Spring Boot, JPA 활용 (김영한)

김영한님의 "실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발" 강의를 듣고 학습한 내용을 정리합니다.  



## JPA와 DB 설정
   - ddl-auto: create  
    
> 자동으로 table을 만들어주는 mode, Application 실행 시점에 내가 가지고 있는 정보를 모두 지우고 다시 생성함.  
실행시점에 모든 테이블을 지우고 재생성하기때문에 운영 환경에서는 절대 create 모드를 사용하면 안됨.  
내렸다 다시 올리면 기존 데이터가 몽땅 사라지는 매직..

 -   org.hibernate.SQL: debug
    
   > show_sql과의 차이점 : show_sql은 System.out으로 log를 찍고, org.hibernate.SQL은 logger를 활용해서 log를 찍음

## 도메인 모델과 테이블 설계

**Join**
> - OneToOne 관계의 경우 어느쪽에 FK를 둬도 상관은 없음, 보통 액세스를 많이 하는쪽에 FK를 지정함.
> - FK와 가까운 곳을 주인으로 지정하면 됨. 
> 주인이 아닌 쪽에 MappedBy를 지정해줌. (mappedBy = "member") Order Table에 있는 member 필드를 통해서 매핑된거라는 의미
> - FK 를 꼭 지정해야하는지? 시스템마다, 상황마다 다르다.  
	시스템의 실시간 트래픽이 굉장히 중요하고 정합성보다는 성능 등 유연하게 잘 서비스 되는것이  
더 중요한 시스템의 경우, FK를 지정하지 않고 Index만 잘 구성해주면 됨.  
>돈과 관련된 너무 중요한 데이터이기 때문에 정합성이 잘 맞아야한다 이런 이유가 있다면  
FK 지정에 대해 진지하게 고민해볼 필요가 있음.

**상속 관계 매핑 전략**
> @Inheritance(strategy = InheritanceType.SINGLE_TABLE)  
- JOINED - 가장 정규화된 스타일  
- SINGLE_TABLE - 한 테이블에 모든 정보를 다 넣음.  
- TABLE_PER_CLASS - CLASS 별로 각각 TABLE을 만듬.  

@DiscriminatorColumn(name = "dtype")  
SINGLE_TABLE 전략을 사용하여 모든 CLASS 정보들을 한 테이블에 넣을거기 때문에 구분값으로 사용할 컬럼을 지정해준다.

**@Repository**
> Repository는 DAO와 비슷한 개념이라고 보면 됨.  
@Repository 어노테이션은 스프링이 제공하는 기본타입, 컴포넌트 스캔의 대상이 되는 어노테이션  
컴포넌트 스캔의 대상이 되서 자동으로 스프링 빈에 등록이 됨.

**기본키 생성**
> 기본키 자동 생성 방법  
ex) @GeneratedValue(strategy = GenerationType.IDENTITY) 와 같은 방식으로 표기한다.  

- IDENTITY - 기본 키 생성을 데이터베이스에 위임함(데이터베이스 의존적)  
  사용 DB) MySQL, PostgreSQL, SQL Server, DB2  
  
- SEQUENCE - 데이터베이스 시퀀스를 사용해서 기본키를 할당함.(데이터베이스 의존적)  
 @SequenceGenerator를 사용하여 시퀀스 생성기를 등록하고, 실제 데이터베이스의 생성될 시퀀스이름을 지정해줘야 함.  
  사용 DB) 시퀀스를 지원하는 Oracle, PostgreSQL, DB2, H2  
- TABLE - 키 생성 테이블을 사용함.  
  키 생성 테이블을 하나 만들고 이름과 값으로 사용할 컬럼을 만드는 방법, 테이블을 사용하기 때문에 데이터베이스 벤더에 상관없이 모든 데이터베이스 적용이 가능함.  
- AUTO - 데이터베이스 벤더에 의존하지 않고 데이터베이스는 기본키를 할당함.  
  데이터베이스에 따라서 위 3가지 방법 중 하나를 자동으로 할당해줌  
 Oracle일 경우 SEQUENCE 방식을 할당, 데이터베이스가 변경되어도 코드를 수정할 필요가 없음.  

기본 키를 직접 할당하기 위해서는 @Id 어노테이션만 사용하면 되고, 자동 생성 전략을 사용하기 위해서는 @Id에 @GeneratedValue를 추가하고 원하는 키 생성 전략을 선택하면 됨.

**@Enumerated**
> EnumType은 (EnumType.ORDINAL), (EnumType.STRING) 두가지가 있음.
- ORDINAL - enum value를 1, 2, 3, .... sequential 하게 지정한다. 중간에 enum value가 추가될 경우, 순서가 맞지 않아 장애가 발생할 수 있으므로 운영 환경에 적합하지 않음.
- STRING - enum value를 string 그대로 지정한다.

ORDINAL을 쓰지말고 STRING을 써야함.

**자바 기본 생성자**
> JAP 스펙 상 엔티티나 임베디드 타입(@Embeddable)은  
자바 기본 생성자(Default Constructor)를 public 또는 protected 로 설정해야한다.  
이런 제약을 두는 이유는 **JPA 구현 라이브러리가 객체를 생성할 때 리플렉션 같은 기술을 사용할 수 있도록 지원해야 하기 때문**   
`리플렉션은 구체적인 클래스 타입을 알지 못해도, 그 클래스의 메소드, 타입, 변수들에 접근할 수 있도록 해주는 자바 API `  


JPA의 기본 스펙은 엔티티에 기본 생성자가 필수로 있어야 한다. 하지만 하이버네이트 같은 구현체들은 조금 더 유연하게 바이트 코드를  조작하는 라이브러리등을 통해서 이런 문제를 회피한다. 완벽한 해결책은 아니므로 상황에 따라 다르다.  
기본 가이드를 따라 기본 생성자를 넣어주어야 한다.  

**엔티티 설계시 주의점**

- 엔티티에는 가급적 Setter를 사용하지 X , 별도의 create 비즈니스 로직을 만들어야 함 
   Setter가 모두 열려있다면, 변경 포인트가 너무 많기 때문에 유지보수가 어렵다.
 - 모든 연관관계는 지연 로딩(`LAZY`)으로 설정하자.
    >즉시로딩(`EAGER`) 은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어려움.   
        JPQL을 실행할 때 N+1 문제 자주 발생  
        하나의 엔티티를 조회하면 연관된 엔티티를 모두 DB 에서 끌고옴. -> 사용하지 않는것이 좋음  
    
    실무에서 모든 연관관계는 지연로딩으로 설정해야한다.  
    지연로딩으로 설정해도 연관된 데이터를 DB에서 조회할 수 있음 -> fetch join or 엔티티 그래프 사용  
    @XToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이기 때문에 직접 지연로딩으로 설정해야함.  
    (@XToMany는 기본 값이 지연로딩임)  

   보통 즉시로딩을 사용하는 이유 : Transaction 밖에서 Lazy loading이 실행되지 않아서..  
   -> 트랜잭션을 좀 빨리 가져오거나 Open-Session-In-View 를 통해 해결할 수 있음.  
   궁극적으로 fetch join을 통해 모두 해결할 수 있음.  
   ```
   Open-Session-In-View (Default는 true) : OSIV 설정을 true 로 지정했을 경우,  
   사용자에게 응답 또는 View가 렌더링 될 때까지 영속성 컨텍스트를 유지함  
   -> 영속성 컨텍스트를 유지한다는 것은 곧 DB Connection을 계속 소유하고 있다는 것  
   실시간 트래픽이 중요한 어플리케이션에서는 문제가 될 수 있다. 

- 컬렉션은 필드에서 초기화  
   > 하이버네이트는 엔티티를 영속화할때, 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경  
   임의의 메서드에서 컬렉션을 잘못 생성하면 하이버네이트 내부 메커니즘에 문제가 발생할 수 있으니 필드 레벨에서 초기화 하는 것이 가장 안전하고 코드도 간결, null 문제에서도 자유로울 수 있음.

- 테이블, 컬럼명 생성 전략  
  하이버네이트 기존 구현 : 엔티티의 필드명을 그대로 테이블 명으로 사용  
  (SpringPhysicalNamingStrategy)  
  스프링 부트 신규 설정 : 엔티티(필드) -> 테이블(컬럼)  
   1. 카멜 케이스 -> 언더스코어 (memberPoint -> member_point)  
   2. .(dot) -> _(underScore)  
   3. 대문자 -> 소문자  
  
    사내에 전사 전략이 정해져 있는 경우, SpringPhysicalNamingStrategy를 참고하여 수정, 적용할 수 있음.  
    1. 논리명 생성 : 명시적으로 컬럼, 테이블명을 직접 적지 않으면 implicitNamingStrategy 사용  
    2. 물리명 생성 : 컬럼, 테이블명을 명시하거나 명시하지 않거나 모든 상황에  적용됨.   

- Cascade 전략  
  모든 Entity는 저장하려면 각각 Persist 해야함.  
  하지만 Cascade 전략을 ALL로 지정했을때는 Persist를 전파하기 때문에 각각 할 필요가 없음.  
  Delete 할때도 함께 지움.  
  ```java
  public class Order {
	  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	  private List<OrderItem> orderItems = new ArrayList<>();

     //위와 같은 구조일 경우 Cascade 전략을 ALL로 지정하지 않았을 경우
     persist(orderItemA)
     persist(orderItemB)
     persist(orderItemC)
     persist(order)

     //Cascade 전략을 ALL 로 지정했을 경우
     persist(order)
  }
