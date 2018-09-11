# 总体介绍
HashTable继承自Dictionary抽象类，实现了Map接口。Dictionary比Map多了两个方法keys()和elements();

## 数据结构
HashTable使用hash表存储元素，**Java *HashTable*采用冲突链表方式**解决hash冲突。
![HashMap_base](../PNGFigures/HashMap_base.png)

## 并发
HashTable是线程同步的，使用synchronized关键字修饰方法，使用的锁是类实例。

## 性能
由于HashTable线程同步，使用的锁是类实例，同步方法不能同时执行，效率低下；在不需要多线程的情况下可以使用HashMap,多线程情况使用ConcurrentHashMap。

## 遍历
由于继承了Dictionary类，Hashtable提供了直接获取迭代器的方法，HashMap中只能通过视图获取迭代器。

# 源码分析
## 成员变量
与HashMap类似的思路，使用Hash表存放entry。Hash冲突的解决方式也是冲突链表。
```java
public class Hashtable<K,V>
    extends Dictionary<K,V>
    implements Map<K,V>, Cloneable, java.io.Serializable {
    private transient Entry<?,?>[] table;//存放entry的数组
    private transient int count;//元素个数
    private int threshold;//临界值
    private float loadFactor;//填充因子
    private transient int modCount = 0;//结构修改次数
    
    private static class Entry<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Entry<K,V> next;
        
        protected Entry(int hash, K key, V value, Entry<K,V> next) {
            this.hash = hash;
            this.key =  key;
            this.value = value;
            this.next = next;
        }
        ......
    }
}
```
## 重要方法
### get()
get方法是线程同步方法，根据指定的key值返回对应的value，思路是通过hash值与数组长度取余得到数组索引，再循环冲突链表，通过`key.equals(k)`方法来判断是否是要找的那个`entry`。
思路与HashMap基本类似

```java
public synchronized V get(Object key) {
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<?,?> e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && e.key.equals(key)) {
            return (V)e.value;
        }
    }
    return null;
}
```

### put()
put()方法内部调用了addEntry()把映射存入hash表。

```java
public synchronized V put(K key, V value) {
    if (value == null) {
        throw new NullPointerException();
    }

    //先通过hash值找到对应的bucket下标，循环冲突链表，若key已经存在，替换值，直接返回
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    Entry<K,V> entry = (Entry<K,V>)tab[index];
    for(; entry != null ; entry = entry.next) {
        if ((entry.hash == hash) && entry.key.equals(key)) {
            V old = entry.value;
            entry.value = value;
            return old;
        }
    }

    //hashtable中未找到该key，将key插入到链表头部
    addEntry(hash, key, value, index);
    return null;
}

private void addEntry(int hash, K key, V value, int index) {
    modCount++;

    Entry<?,?> tab[] = table;
    if (count >= threshold) {
        // 数组扩容
        rehash();

        tab = table;
        hash = key.hashCode();
        index = (hash & 0x7FFFFFFF) % tab.length;//重新计算key对应的bucket的下标
    }

    // 新建一个entry，放在链表头部
    Entry<K,V> e = (Entry<K,V>) tab[index];
    tab[index] = new Entry<>(hash, key, value, e);
    count++;
}
```
### rehash（）
先将数组扩容为原先的两倍+1，循环数组，循环链表，对每个元素重新计算index，调整到新的位置

```java
protected void rehash() {
    int oldCapacity = table.length;
    Entry<?,?>[] oldMap = table;

    // overflow-conscious code
    int newCapacity = (oldCapacity << 1) + 1; //newCapacity = oldCapacity*2 + 1
    if (newCapacity - MAX_ARRAY_SIZE > 0) {
        if (oldCapacity == MAX_ARRAY_SIZE)
            // Keep running with MAX_ARRAY_SIZE buckets
            return;
        newCapacity = MAX_ARRAY_SIZE;
    }
    Entry<?,?>[] newMap = new Entry<?,?>[newCapacity];

    modCount++;
    threshold = (int)Math.min(newCapacity * loadFactor, MAX_ARRAY_SIZE + 1);
    table = newMap;

    for (int i = oldCapacity ; i-- > 0 ;) { //循环数组
        for (Entry<K,V> old = (Entry<K,V>)oldMap[i] ; old != null ; ) { //循环链表
            Entry<K,V> e = old;
            old = old.next;

            int index = (e.hash & 0x7FFFFFFF) % newCapacity;
            e.next = (Entry<K,V>)newMap[index]; //将元素放到链表头部
            newMap[index] = e;
        }
    }
}
```
### remove()
remove()的作用是删除`key`值对应的`entry`，思路是先根据key的hash值和数组长度获取到bucket下标，循环冲突链表，通过key.equals(k)方法找到需要删除的entry，删除entry。

```java
public synchronized V remove(Object key) {
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;//计算bucket下标
    Entry<K,V> e = (Entry<K,V>)tab[index];
    for(Entry<K,V> prev = null ; e != null ; prev = e, e = e.next) {//循环冲突链表
        if ((e.hash == hash) && e.key.equals(key)) {//找到了需要删除的entry
            modCount++;
            if (prev != null) {//删除的entry不是链表头部
                prev.next = e.next;
            } else {//删除的entry是在链表头部
                tab[index] = e.next;
            }
            count--;
            V oldValue = e.value;
            e.value = null;
            return oldValue;//返回删除entry的值
        }
    }
    return null;
}
```
## 视图
### KeySet
keySet()方法返回的视图使用Collections.synchronizedSet()进行包装。
```java
private class KeySet extends AbstractSet<K> {
    //获取key的迭代器
    public Iterator<K> iterator() {
        return getIterator(KEYS);
    }
    ...
}
```
### ValueCollection
value()方法返回的视图使用Collections.synchronizedCollection()进行包装。
```java
private class ValueCollection extends AbstractCollection<V> {
    //获取value的迭代器
    public Iterator<V> iterator() {
        return getIterator(VALUES);
    }
    ...
}
```
### EntrySet
```java
private class EntrySet extends AbstractSet<Map.Entry<K,V>> {
    
    ////获取entry的迭代器
    public Iterator<Map.Entry<K,V>> iterator() {
        return getIterator(ENTRIES);
    }
    ...
}
```
## 迭代器
迭代器获取方式：
- 通过Dictionary的keys()elements()获取
- 通过上诉视图的iterator()方法获取
上面几种方式获取的都是Enumerator的对象，通过type字段来区分。

### Enumerator
Enumerator实现了Enumeration和Iterator接口，所以除了Iterator接口的hasNext()，next()和remove()方法外，还有hasMoreElements()和nextElement()方法;<br>
不过hasNext()内部调用了hasMoreElements()，next()内部调用了nextElement()。
```java
private class Enumerator<T> implements Enumeration<T>, Iterator<T> {
    Entry<?,?>[] table = Hashtable.this.table;
    int index = table.length;
    Entry<?,?> entry = null;
    Entry<?,?> lastReturned = null;
    int type;
    boolean iterator;
    protected int expectedModCount = modCount;
    
    Enumerator(int type, boolean iterator) {
        this.type = type;
        this.iterator = iterator;
    }
}
```

## 与HashMap的不同之处
- Hashtable里的方法是线程同步的。
- Hashtable不能存储null键和null值。
- 计算bucket下标的方式不一样，Hashtable直接使用元素的hashCode()返回值作为hash值，将hash值对数组长度取余，余数作为下标；HashMap对hashCode重新做了异或运算，用新的hash值和(数组长度-1)做与运算，值作为下标。
- Hashtable采用链表存储hash冲突的元素，HashMap使用链表和红黑树(1.8)。
- Hashtable将元素挂在链表头部，HashMap挂在尾部或按红黑树添加(1.8)。
- Hashtable默认的初始大小为11，每次扩容变为原来的2n+1。HashMap默认的初始化大小为16，每次扩充变为原来的2倍，HashMap数组长度必定为2的幂次方
- Hashtable继承了Dictionary，提供了直接获取迭代器的方法keys()和elements()，HashMap只能通过视图获取迭代器。
