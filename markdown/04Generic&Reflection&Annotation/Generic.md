# 泛型定义
泛型的本质是参数化类型，即给类型指定一个参数，然后在使用时再指定此参数具体的值，那样这个类型就可以在使用时决定了。
这种参数类型可以用在类、接口和方法中，分别被称为泛型类、泛型接口、泛型方法。

# 泛型好处
- 编译期类型检查，提高代码的可读性和安全性
- 避免代码中的强制类型转换
- 代码可以被不同类型的对象重用，提高复用率

# 泛型类
```java
public class Demo<T>{
    private T t;

    class SubDemo<T> extends Demo<T>{
        
    }

}
```
# 泛型接口
```java
public interface Demo<T>{
    
}

class SubDemo<T> implements Demo<T>{
    
}
```

# 泛型方法
```java
public class Demo<T>{
    
    public T getK(T t){
        return t;
    }
    
    public <K> K get(K k){
        return k;
    }
    
}
```

# 通配符

## 通配符
```java
public class Demo{
    void test(List<?> list){
    }
}
```

## 通配符上界
```java
public class Demo<E extends Object>{
    
    void test(List<? extends E> list){
    }
    
    void test(List<? extends Object> list){
    }
    
    <T extends Object> T test2(T t){
        return t;
    }
}
```

## 通配符下界
```java
public class CommonResource<E> {
    void test(List<? super E> list){
    }

    void test1(List<? super Object> list){
    }
}
```

泛型类中的静态方法和静态变量不可以使用泛型类所声明的泛型类型参数

