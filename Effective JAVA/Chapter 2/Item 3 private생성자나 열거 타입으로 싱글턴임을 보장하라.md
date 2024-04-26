# private생성자나 열거 타입으로 싱글턴임을 보장하라
- 싱글턴이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다.
- 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다.
  - 타입을 인터페이스로 정의한 다음 그 인터페이스를 구현해서 만든 싱글턴이 아니라면 싱글턴 인스턴스를 가짜 구현으로 대체할 수 없기 때문이다.
## 싱글턴 생성 방법
### private 생성자와 public static 멤버가 final 필드
``` java
public class Singleton {
    public static final Singleton INSTANCE = new Singleton();
    private Singleton(){}
    
    ...
}
```
- 생성자는 필드인 INSTANCE를 초기화할 때 한번만 호출되고 클래스 외부에서 생성될 생성자가 없기 때문에 인스턴스가 한개임이 보장된다.
- 이 방식의 장점 중 첫 번째는 해당 클래스가 싱글턴인 것이 API에 명확하게 드러난다는 것이다.
  - public static final이니 다른 객체를 참조할 수 없다.
- 두 번째 장점은 간결함이다.
### 정적 팩토리 방식의 싱글턴
``` java
public class Singleton {
    private static Singleton INSTANCE = new Singleton();
    private Singleton(){}
    public static Singleton getInstance(){return INSTANCE;}

    ...
}
```
- 이 방식의 장점은 다음과 같다.
1. API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다.
  - 유일한 인스턴스를 반환하던 팩토리 메소드가 호출하는 스레드별로 다른 인스턴스를 넘겨주게 할 수도 있다.
2. 원한다면 정적 팩토리를 제네릭 싱글턴 팩토리로 만들 수 있다.
3. 정적 팩토리의 메소드 참조를 공급자로 사용할 수 있다.
### 원소가 하나인 enum타입을 선언한다.
- public 필드 방식과 비슷하지만 더 간결하고 추가 작업 없이도 직렬화를 할 수 있으며 아주 복잡한 직렬화 상황이나 리플렉션 공격에서도 제 2의 인스턴스가 생기는 일을 막아준다.
  - 1번이나 2번의 방식으로 만든 싱글턴 클래스를 직렬화하려면 단순히 Serializabl을 구현한다고 선언하는 것만으로는 부족하다.
  - 모든 인스턴스 필드를 transient(일시적)라고 선언하고 readResolve 메소드를 제공해야한다.
  - 이렇게 하지 않으면 직렬화된 인스턴스를 역직렬화할 때마다 새로운 인스턴스가 만들어진다. (가짜 Singleton 인스턴스가 추가될 수도 있다.
  - 가짜 인스턴스 생성 예방을 위한 readResolve()메소드
    ```java
    private Object readResolve() {
        // 진짜 인스턴스를 반환하고 가짜 인스턴스는 가비지 컬렉터에 맡김
        return INSTANCE;
    }
    ```
- enum 타입으로 선언
``` java
public enum Singleton {
    INSTANCE;
    public static Singleton getInstance(){return INSTANCE;}

    ...
}
```
# 정리
- 대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.
- 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.
  - 열거 타입이 다른 인터페이스를 구현하도록 선언할 수는 있다.
