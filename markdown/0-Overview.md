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
    
    //1.8新增接口
    // 循环
    default void forEach(Consumer<? super T> action) {}
    
    //返回并行遍历迭代器
    default Spliterator<T> spliterator() {}
}
```

## Collection
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
    
    //添加指定集合中的所有元素
    boolean addAll(Collection<? extends E> c);
    
    //移除指定元素
    boolean remove(Object o);
    
    //移除指定集合中的所有元素
    boolean removeAll(Collection<?> c);
    
    //与指定集合取交集
    boolean retainAll(Collection<?> c);
    
    //清除所有元素
    void clear();
    
    //判断元素是否相同
    boolean equals(Object o);
    
    //返回hashCode
    int hashCode();
    
    //1.8新增接口
    // 删除满足条件的元素
    default boolean removeIf(Predicate<? super E> filter) {}
    
    // 返回并行遍历迭代器
    default Spliterator<E> spliterator() {}
    
    //返回数据视图
    default Stream<E> stream() {}
    
    //返回并行数据视图
    default Stream<E> parallelStream() {}
}
```

## List
继承结构：
```java
public interface List<E> extends Collection<E> {
    
    //在指定位置添加元素
    void add(int index, E element);
    
    //在指定位置添加所有集合中的元素
    boolean addAll(int index, Collection<? extends E> c);
    
    //删除指定索引的元素
    E remove(int index);
    
    //获取指定索引的元素
    E get(int index);
    
    //获取指定元素的索引
    int indexOf(Object o);

    //获取指定元素最后一个匹配的索引
    int lastIndexOf(Object o);
    
    //获取迭代器
    ListIterator<E> listIterator();

    //获取从指定位置开始的迭代器
    ListIterator<E> listIterator(int index);
    
    //获取子视图
    List<E> subList(int fromIndex, int toIndex);
    
    //1.8新增接口 
    // 对每个元素执行operator指定的操作，并用操作结果来替换原来的元素。
    default void replaceAll(UnaryOperator<E> operator) {}
    
    //根据c指定的比较规则对容器元素进行排序
    default void sort(Comparator<? super E> c) {}
}
```

## Queue
```java

```
## BlockingQueue
## Deque
## BlockingDeque


## Set
```java
public interface Set<E> extends Collection<E> {
    //与Collection相同
}
```
## SortedSet
```java
```
## NavigableSet


## Map
```java
public interface Map<K,V> {
    
    //返回元素个数
    int size();
    
    //返回元素是否为空
    boolean isEmpty();
    
    //是否包含指定的键
    boolean containsKey(Object key);

    //是否包含指定的值
    boolean containsValue(Object value);
    
    V get(Object key);
    
    V put(K key, V value);
    
    V remove(Object key);
    
    void putAll(Map<? extends K, ? extends V> m);
    
    void clear();
    
    Set<K> keySet();
    
    Collection<V> values();
    
    Set<Map.Entry<K, V>> entrySet();
    
    boolean equals(Object o);
    
    int hashCode();
    
    //1.8新增接口
    default V getOrDefault(Object key, V defaultValue) {};
    
    default void forEach(BiConsumer<? super K, ? super V> action) {};
    
    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {};
    
    default V putIfAbsent(K key, V value) {};
    
    default boolean remove(Object key, Object value){};
    
    default boolean replace(K key, V oldValue, V newValue) {};
    
    default V replace(K key, V value) {};
    
    default V computeIfAbsent(K key,
            Function<? super K, ? extends V> mappingFunction) {};

    default V computeIfPresent(K key,
            BiFunction<? super K, ? super V, ? extends V> remappingFunction) {};
    
    default V compute(K key,
                 BiFunction<? super K, ? super V, ? extends V> remappingFunction) {};
    
    default V merge(K key, V value,
                BiFunction<? super V, ? super V, ? extends V> remappingFunction) {};
}    
```

## SortedMap
## NavigableMap
## ConcurrentMap
## ConcurrentNavigableMap





