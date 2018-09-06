# Java集合框架综述

# 概览

容器，就是可以容纳其他Java对象的对象。*Java Collections Framework（JCF）*为Java开发者提供了通用的容器，其始于JDK 1.2，优点是：

- 降低编程难度
- 提高程序性能
- 提高API间的互操作性
- 降低学习难度
- 降低设计和实现相关API的难度
- 增加程序的重用性

Java容器里只能放对象，对于基本类型（int, long, float, double等），需要将其包装成对象类型后（Integer, Long, Float, Double等）才能放到容器里。很多时候拆包装和解包装能够自动完成。这虽然会导致额外的性能和空间开销，但简化了设计和编程。


# 集合框架图

为了规范容器的行为，统一设计，JCF定义了14种容器接口（collection interfaces），它们的关系如下图所示：
![pic](../PNGFigures/JCF_Collection_Interfaces.png)
*Map*接口没有继承自*Collection*接口，因为*Map*表示的是关联式容器而不是集合。但Java为我们提供了从*Map*转换到*Collection*的方法，可以方便的将*Map*切换到集合视图。
上图中提供了*Queue*接口，却没有*Stack*，这是因为*Stack*的功能已被JDK 1.6引入的*Deque*取代。

# 接口说明
## Iterable
```java
public interface Iterable<T> {
    //返回遍历迭代器
    Iterator<T> iterator();
    
    //1.8新增接口，循环
    default void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            action.accept(t);
        }
    }
    
    //1.8新增接口，返回并行遍历迭代器
    default Spliterator<T> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), 0);
    }
}
```

## Collection
继承结构：
```java
public interface Collection<E> extends Iterable<E>{}
```
方法说明：
```java
public interface Collection<E> extends Iterable<E> {
    //返回元素个数
    int size();
    
    //返回元素是否为空
    boolean isEmpty();
    
    //是否包含某个元素
    boolean contains(Object o);
    
    boolean containsAll(Collection<?> c);
    
    //返回用于遍历的迭代器
    Iterator<E> iterator();
    
    //集合转数组
    Object[] toArray();
    
    //集合转数组，元素放在给定的数组中
    <T> T[] toArray(T[] a);
    
    //添加元素
    boolean add(E e);
    
    boolean addAll(Collection<? extends E> c);
    
    boolean remove(Object o);
    
    boolean removeAll(Collection<?> c);
    
    boolean retainAll(Collection<?> c);
    
    void clear();
    
    boolean equals(Object o);
    
    int hashCode();
    
    default boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }
    
    default Spliterator<E> spliterator() {
        return Spliterators.spliterator(this, 0);
    }
    
    default Stream<E> stream() {
            return StreamSupport.stream(spliterator(), false);
    }
    
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
```

## List
继承结构：
```java

```

## Set
## SortedSet
## NavigableSet
## Queue
## BlockingQueue
## Deque
## BlockingDeque

## Map
## SortedMap
## NavigableMap
## ConcurrentMap
## ConcurrentNavigableMap

