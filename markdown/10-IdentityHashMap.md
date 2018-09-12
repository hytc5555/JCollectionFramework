# 总体介绍
实现了Map接口，使用hash表存储键和值，元素是否相等通过地址比较（==）来实现。

## 数据结构
使用hash表，处理hash冲突的方式为，向后遍历（每次索引增加2），直到有空位，将键和值保存在连续的空间。
数组大小总是2的幂次方，每次扩容为原先的两倍。
## 并发
线程不安全
## 性能
`put()`和`get()`方法可以在常数时间内完成，若要扩容，需要rehash，较为耗时。
## 遍历
遍历数组

# 源码分析
## 成员变量
```java
public class IdentityHashMap<K,V>
    extends AbstractMap<K,V>
    implements Map<K,V>, java.io.Serializable, Cloneable{
    
    // 缺省容量大小
    private static final int DEFAULT_CAPACITY = 32;
    // 最小容量
    private static final int MINIMUM_CAPACITY = 4;
    // 最大容量
    private static final int MAXIMUM_CAPACITY = 1 << 29;
    // 用于存储实际元素的表
    transient Object[] table;
    // 大小
    int size;
    // 对Map进行结构性修改的次数
    transient int modCount;
    // null key所对应的值
    static final Object NULL_KEY = new Object();
}
```

## 重要方法
### put()
 使用hash方法得到key对应的下标，若有hash冲突，通过==判断key和元素的地址是否相同，若相同替换旧值，否则下标+2，循环直到空位置i，将键和值分别存入i和i+1位置。
若添加元素后，容量达到了临界值，扩容数组，每次扩为原来的两倍，扩容时需要对原有元素重新计算索引。

```java
public V put(K key, V value) {
    Object k = maskNull(key);
    Object[] tab = table;
    int len = tab.length;
    int i = hash(k, len);

    Object item;
    while ( (item = tab[i]) != null) {
        if (item == k) {
                V oldValue = (V) tab[i + 1];
            tab[i + 1] = value;
            return oldValue;
        }
        i = nextKeyIndex(i, len);
    }

    modCount++;
    tab[i] = k;
    tab[i + 1] = value;
    if (++size >= threshold)
        resize(len); // len == 2 * current capacity.
    return null;
}

```
### get()
通过hash()方法计算索引，若索引上有元素，判断是否是要找的元素，否则每次索引加2，遍历数组直到空位置。
```java
public V get(Object key) {
    Object k = maskNull(key);
    Object[] tab = table;
    int len = tab.length;
    int i = hash(k, len);
    while (true) {
        Object item = tab[i];
        if (item == k)
            return (V) tab[i + 1];
        if (item == null)
            return null;
        i = nextKeyIndex(i, len);
    }
}
```
### remove()
```java
public V remove(Object key) {
    Object k = maskNull(key);
    Object[] tab = table;
    int len = tab.length;
    int i = hash(k, len);

    while (true) {
        Object item = tab[i];
        if (item == k) {
            modCount++;
            size--;
                V oldValue = (V) tab[i + 1];
            tab[i + 1] = null;
            tab[i] = null;
            //hash冲突的元素需要保证存储连续，需要将删除元素后的前移
            closeDeletion(i);
            return oldValue;
        }
        if (item == null)
            return null;
        i = nextKeyIndex(i, len);
    }

}
```
