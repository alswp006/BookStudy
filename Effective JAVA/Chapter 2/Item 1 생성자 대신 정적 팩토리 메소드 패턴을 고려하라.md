# 생성자 대신 정적 팩토리 메소드를 고려하라.
# 정적 팩토리 메소드를 왜 쓰는가?

- 정적 팩토리 메소드의 장점은 다음과 같습니다.
1. 이름을 가질 수 있다.
2. 호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.
3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.
4. 입력 매개변수가 매번 다른 클래스의 객체를 반환할 수 있다.
5. 정적 팩토리 메소드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.
- 이를 정리해보도록 하겠습니다.

## 1. 이름을 가질 수 있다.

- 얼마 전 우테코 프리코스를 진행하며 인상적이었던 4주차 미션에서 할인 클래스를 예로 들겠습니다.
- 다음과 같이 할인 적용 여부, 할인 금액, 총 금액을 갖고 있는 Discount 클래스가 있다고 생각해봅시다.

```python
public class Discount {
    private boolean isApplicable;
    private int discountPrice;
    private int totalPrice;
}
```

- 생성자를 통해 위 필드에 값을 넣어줄 수 있을 것입니다.
- 하지만 매개변수를 여러 방식으로 받는 생성자들을 만들기 위해 아래와 같이 만들면 에러가 날 것입니다.

```python
public class Discount {
    private boolean isApplicable;
    private int discountPrice;
    private int totalPrice;

    public Discount(boolean isApplicable, int discountPrice){
        this.isApplicable = isApplicable;
        this.discountPrice = discountPrice;
    }

    public Discount(boolean isApplicable, int totalPrice){
        this.isApplicable = isApplicable;
        this.totalPrice = totalPrice;
    }
}
```

- 위 생성자에서 두 번째 매개변수로 int타입의 변수를 받아도 discountPrice인지 totalPrice인지 알 길이 없기 때문이겠죠?
- 위와 같은 경우 매개변수의 순서를 바꾸어 에러가 발생하지 않게 만들 수 있겠지만 이렇게 만들면 개발자가 사용하기 굉장히 어려워질 것입니다.
- 이를 아래와 같이 정적 팩토리 메소드를 사용하면 해결할 수 있습니다!

```python
public class Discount {
    private boolean isApplicable;
    private int discountPrice;
    private int totalPrice;

    public static Discount discountPrice(boolean isApplicable){
        Discount discount = new Discount();
        discount.isApplicable = isApplicable;
        discount.discountPrice = 1000;
        return discount;
    }

    public static Discount totalPrice(boolean isApplicable){
        Discount discount = new Discount();
        discount.isApplicable = isApplicable;
        discount.totalPrice = 20000;
        return discount;
    }
}
```

- 정적 팩토리 메소드를 사용하면 이름을 부여하여 메소드의 의미도 명확해지고 매개변수가 같은 타입으로 있어도 자유롭게 사용할 수 있습니다.

## 2. 호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.

- 클래스의 생성자는 호출할 때마다 생성자가 생길 것입니다.
- 아래와 같은 코드가 있습니다.

```python
public class Discount {
    private boolean isApplicable;
    private int discountPrice;
    private int totalPrice;
}

public class Main {
		public static void main(String[] args){
				System.out.println(new Discount());
				System.out.println(new Discount());
				System.out.println(new Discount());
		}
}

//출력 결과
abstr.Discount@2a84aee7
abstr.Discount@a09ee92
abstr.Discount@30f39991
```

- 하지만 경우에 따라 인스턴스를 특정한 경우에 새로 만들고 싶을 때 통제를 할 수 있어야 합니다.
- 아래와 같이 사용하면 인스턴스를 한 번만 생성하도록 할 수 있습니다.(싱글톤 패턴..?)

```python
public class Discount {
    private boolean isApplicable;
    private int discountPrice;
    private int totalPrice;

    private Discount() {
    }

    private static final Discount DISCOUNT = new Discount();

    public static Discount newInstance() {
        return DISCOUNT;
    }
}

public class Main {
    public static void main(String[] args) {
        Discount discount1 = Discount.newInstance();
        Discount discount2 = Discount.newInstance();

        System.out.println(discount1);
        System.out.println(discount2);
    }
}

//출력 결과
abstr.Discount@a09ee92
abstr.Discount@a09ee92
```

- 위 방법과 달리 List나 Map과 같은 자료구조에 필요한 갯수로 인스턴스를 생성해놓으면 인스턴스를 필요한 만큼 생성할 수도 있습니다.
- 이와 같이 정적 팩토리 메소드를 사용하면 새로운 인스턴스 생성을 막을 수 있고 정해진 범위를 벗어나는 인스턴스의 생성을 막을 수 있습니다.
- 이 방법은 싱글톤 패턴과 유사한 것 같습니다.

## 3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

- 원래 생성자는 해당하는 클래스의 인스턴스만 만들어주지만 정적 팩토리 메소드는 반환 타입의 호환 가능한 자식 클래스들의 인스턴스도 조건별로 만들어줄 수 있습니다.

```java
public interface Discount {
    String discount();
}

public class WeekDiscount implements Discount{
    @Override
    public String discount() {
        return "weekDiscount";
    }
}

public class ChristmasDiscount implements Discount{
    @Override
    public String discount() {
        return "merry Christmas";
    }
}

public class DiscountFactory {
    public static Discount of(String discountType) {
        if (discountType.equals("Christmas")) {
            return new ChristmasDiscount();
        } else {
            return new WeekDiscount();
        }
    }
}
```

- 위에서 볼 수 있듯이 인스턴스 생성에 대해 고정적이지 않고 유연합니다.

## 4. 입력 매개변수가 매번 다른 클래스의 객체를 반환할 수 있다.

- 4번의 장점과 연계되는 장점입니다!
- Discount와 DiscountFactory를 적절히 사용하면 아래와 같이 매개변수에 따라 다른 인스턴스를 사용할 수 있습니다!

```java
public class Main {
    public static void main(String[] args) {
        Discount discount = DiscountFactory.of("Christmas");
        String discountType = discount.discount();
        System.out.println(discountType);

        discount = DiscountFactory.of("1일");
        discountType = discount.discount();
        System.out.println(discountType);
    }
}

//출력
Christmas Discount
Week Discount
```

- 또한 인터페이스에 of 메소드를 넣어주면 별도로 Factory를 사용하지 않아도 된다.

```java
public interface Discount {
    String discount();

    static Discount of(String discountType) {
        if (discountType.equals("Christmas")) {
            return new ChristmasDiscount();
        } else {
            return new WeekDiscount();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Discount discount = Discount.of("Christmas");
        String discountType = discount.discount();
        System.out.println(discountType);
    }
}

//출력
Christmas Discount
```

## 5. 정적 팩토리 메소드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

- 이 장점은 정적 팩토리 메소드가 유연하게 반환 객체의 클래스를 결정할 수 있다는 것을 의미합니다.
    - 예를 들어, 정적 팩토리 메소드가 인터페이스를 반환 유형으로 가지고 있다면, 이 메소드는 실제로는 이 인터페이스를 구현하는 어떤 클래스의 인스턴스라도 반환할 수 있습니다.
    - 이 클래스는 메소드를 작성할 때는 반드시 존재하지 않아도 되며, 나중에 새로운 클래스가 추가되더라도 메소드는 수정 없이 이 새 클래스의 인스턴스를 반환할 수 있습니다.
    - 이렇게 하면, 메소드 사용자는 인터페이스만 알고 있으면 되므로 구현 세부사항에 대해 걱정할 필요가 없습니다. 또한, 반환할 클래스를 변경해야 할 때도 메소드의 사용자는 영향을 받지 않습니다.
- 이 장점은 서비스 제공자 프레임워크를 만드는 근간이 됩니다.
    - 대표적으로 JDBC(Java DataBase Connectivity)가 있습니다.

# 정적 팩토리 메소드 네이밍 컨벤션

- 인스턴스에 설명이 명확히 드러나지 않으니 사용자가 찾기가 어렵습니다. 이에 대한 대책으로 몇가지의 관례적인 네이밍을 사용하기도 합니다.

| 메소드명 | 내용 |
| --- | --- |
| from | 하나의 매개 변수를 받는 객체를 생성 |
| of | 여러개의 매개 변수를 받는 객체를 생성 |
| valueOf | 매개변수를 받아 해당 타입의 인스턴스를 반환 |
| instance || getInstance | 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지 않는다. |
| create || newInstance | 새로운 인스턴스의 생성을 보장한다. |
| get(Type) | 생성할 클래스가 아닌 다른 타입의 인스턴스를 생성한다. "Type"은 반환할 타입의 클래스를 적는다. 같은 인스턴스임을 보장하지 않는다. |
| new(Type) | 생성할 클래스가 아닌 다른 타입의 인스턴스를 생성한다. "Type"은 반환할 타입의 클래스를 적는다. 새로운 인스턴스의 생성을 보장한다. |
