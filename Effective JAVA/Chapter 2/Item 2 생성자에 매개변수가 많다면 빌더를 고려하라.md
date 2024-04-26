# 생성자에 매개변수가 많다면 빌더를 고려하라
- 생성자이든 이전에 공부한 정적 팩토리 메소드이든 매개변수가 너무 많다면 적절히 대응하기 어렵다는 단점이 있습니다.
- 만약 아래와 같이 학생 정보를 담고 있는 클래스가 있다고 생각해봅시다.
``` java
public class Student {
    String name;
    int std_Id;
    int birthDate;
    String address;
    int classInfo;
    int admissionInfo;
}
```
- Student 클래스의 생성자는 어떤 모습일까요?
- 이제부터 알아보겠습니다!
## 1. 점층적 생성자 패턴(telescoping constructor pattern)
- 점층적 생성자 패턴은 필수 매개변수만 받는 생성자부터 필수 매개변수와 선택 매개변수를 1개까지 받는 생성자, 선택 매개변수를 2개 받는 생성자...로 생성자를 늘려가는 방식입니다.
- 이에 대한 코드는 아래와 같습니다.
``` java
public class Student {
    String name;
    int std_Id;
    int birthDate;
    String address;
    int classInfo;
    int admissionInfo;
    public Student(String name, int std_Id){
        this(name, std_Id, 0);
    }
    
    public Student(String name, int std_Id, int birthDate){...}

    ...

    public Student(String name, int std_Id, int birthDate, String address, int classInfo, int admissionInfo) {
        this.name = name;
        this.std_Id = std_Id;
        this.birthDate = birthDate;
        this.address = address;
        this.classInfo = classInfo;
        this.admissionInfo = admissionInfo;
    }
}
```
- 이 경우 매개변수의 수가 늘어난다면 클라이언트 코드를 작성하거나 읽기 어려워집니다.
- 점층적 생성자 패턴 탈락.
## 2. 자바빈즈 패턴(JavaBeans Pattern)
- 자바빈즈 패턴은 매개변수가 없는 생성자로 객체를 만든 후 세터 메소드들을 호출해 원하는 매개변수의 값을 설정하는 방식입니다.
``` JAVA
public class Student {
    String name;
    int std_Id;
    int birthDate;
    String address;
    int classInfo;
    int admissionInfo;
    
    public Student(){}
    public void setName(String name) {this.name = name;}
    public void setStd_Id(int std_Id) {this.std_Id = std_Id;}
    public void setBirthDate(int birthDate) {this.birthDate = birthDate;}
    public void setAddress(String address) {this.address = address;}
    public void setClassInfo(int classInfo) {this.classInfo = classInfo;}
    public void setAdmissionInfo(int admissionInfo) {this.admissionInfo = admissionInfo;}
}
```
- 점층적 생성자 패턴의 단점들이 보완되고 인스턴스를 만들기도 쉽고 가독성도 좋아졌습니다.
- 하지만 자바빈즈 패턴에서는 객체 하나를 만들려면 메소드를 여러 개 호출해야 하고, 객체가 완전히 생성되기 전까지는 일관성이 무너진 상태에 놓이게 됩니다.
- 점층적 생성자 패턴에서는 매개변수들이 유효한지를 생성자에서만 확인하면 일관성을 유지할 수 있었지만 자바빈즈 패턴에서는 그 장치가 사라졌습니다.
- 이러한 문제들때문에 자바빈즈 패턴에서는 클래스를 불변우로 만들 수 없고 스레드 안전성을 얻으려면 프로그래머의 추가 작업이 필요합니다.
- 자바빈즈 패턴 탈락.
## 3. 빌더 패턴
- 다행히도 점층적 생성자 패턴의 안정성과 자바빈즈 패턴의 가독성을 겸비한 빌더 패턴이라는 것이 있습니다.
- 빌더 패턴은 클라이언트가 필요한 객체를 직접 만드는 대신 필수 매개변수만으로 생성자를 호출해 빌더 객체를 얻습니다.
- 그 다음 빌더 객체가 제공하는 일종의 세터 메소드들로 원하는 선택 매개변수들을 설정합니다.
- 마지막으로 매개변수가 없는 build 메소드를 호출해 필요한 객체를 얻습니다.
``` java
public class Student {
    private final String name;
    private final int std_Id;
    private final int birthDate;
    private final String address;
    private final int classInfo;
    private final int admissionInfo;

    public static class Builder{
        private final String name;
        private final int std_Id;
        private int birthDate = 0;
        private String address = null;
        private int classInfo = 0;
        private int admissionInfo = 0;
        public Builder(String name, int std_Id){
            this.name = name;
            this.std_Id = std_Id;
        }

        public Builder birthDate(int val){
            birthDate = val;
            return this;
        }
        public Builder address(String val){
            address = val;
            return this;
        }
        public Builder classInfo(int val){
            classInfo = val;
            return this;
        }
        public Builder admissionInfo(int val){
            admissionInfo = val;
            return this;
        }
        public Student build() {
            return new Student(this);
        }
    }
    private Student(Builder builder){
        name = builder.name;
        std_Id = builder.std_Id;
        birthDate = builder.birthDate;
        address = builder.address;
        classInfo = builder.classInfo;
        admissionInfo = builder.admissionInfo;
    }
}

//main 메소드

public class StudentMain {
    public static void main(String[] args) {
        Student student = new Student.Builder("kimminje", 5500555)
                .birthDate(1999)
                .address("Daegu")
                .classInfo(1)
                .admissionInfo(2018)
                .build();
    }
}
```
- Stdent 클래스는 불변이며 모든 매개변수의 기본값들을 모아뒀습니다.
- 빌더의 세터 메소드들은 빌더 자신을 반환하기 때문에 연쇄적으로 호출이 가능합니다.
- 클라이언트 코드는 쓰기 쉽고 읽기도 쉬워졌습니다.
- 유효성 검사 코드는 생략했지만 잘못된 매개변수를 최대한 일찍 발견하려면 빌더의 생성자와 메소드에서 입력 매개변수를 검사하고 build()메소드가 호출하는 생성자에서 여러 매개변수에 걸친 불변식을 검사해야 합니다.
- 빌더 패턴은 점층적 생성자 패턴보다 클라이언트 코드를 읽고 쓰기가 훨씬 간결하고 자바빈즈보다 훨씬 안전합니다.
- 빌더 패턴 합격.
# 정리
- 생성자나 정적 팩토리가 처리해야할 매개변수가 많다면 빌더 패턴을 사용합시다.
