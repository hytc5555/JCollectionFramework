# 总体介绍
EnumMap继承自AbstractMap，规定键只能使用枚举类型，适用于需要强制指定键类型的场景。

## 数据结构
EnumMap使用枚举类型数组来存储枚举值，每个枚举值在数组中的索引与ordinal()方法返回值保持一致。
使用Object数组来存储值，存储的位置也是枚举的ordinal值。
可以存储null值,EnumMap会将null包装成一个对象存储到数组中。
## 并发
EnumMap不是线程安全的

## 性能
put，remove和get操作都是常数时间。

## 遍历
遍历数组

# 源码分析
## 成员变量
```java
public class EnumMap<K extends Enum<K>, V> extends AbstractMap<K, V>
    implements java.io.Serializable, Cloneable{
    
    private final Class<K> keyType; //枚举类型
    private transient K[] keyUniverse; //枚举值数组
    private transient Object[] vals; //值数组
    private transient int size = 0; //元素个数
}
```
## 重要方法
### put()
```java
public V put(K key, V value) {
    typeCheck(key);
    //获取枚举对应的索引
    int index = key.ordinal();
    Object oldValue = vals[index];
    //若value是null，返回NULL对象
    vals[index] = maskNull(value);
    if (oldValue == null)
        //元素数量加1
        size++;
    return unmaskNull(oldValue);
}
```
### get()
```java
public V get(Object key) {
    //检查key是否合法
    //返回数组中的值
    return (isValidKey(key) ?
            unmaskNull(vals[((Enum<?>)key).ordinal()]) : null);
}
```
### remove
```java
public V remove(Object key) {
    //检测key是否合法，不合法返回false
    if (!isValidKey(key))
        return null;
    //获取索引
    int index = ((Enum<?>)key).ordinal();
    //获取原值
    Object oldValue = vals[index];
    //数值指定位置置为null
    vals[index] = null;
    if (oldValue != null)
        //元素大小-1
        size--;
    //返回原值
    return unmaskNull(oldValue);
}
```

## 视图
### KeySet
```java
private class KeySet extends AbstractSet<K> {
    public Iterator<K> iterator() {
        return new KeyIterator();
    }
}
```

### Values
```java
private class Values extends AbstractCollection<V> {
    public Iterator<V> iterator() {
        return new ValueIterator();
    }
}
```

### EntrySet
```java
private class EntrySet extends AbstractSet<Map.Entry<K,V>> {
    public Iterator<Map.Entry<K,V>> iterator() {
        return new EntryIterator();
    }
}
```

## 迭代器
### KeyIterator
```java
public boolean hasNext() {
    while (index < vals.length && vals[index] == null)
        index++;
    return index != vals.length;
}

public K next() {
    if (!hasNext())
        throw new NoSuchElementException();
    lastReturnedIndex = index++;
    return keyUniverse[lastReturnedIndex];
}
```
