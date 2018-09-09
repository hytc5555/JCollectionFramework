# 总体介绍
ArrayDeque继承自AbstractCollection类，并且实现了Deque接口，说明它是一个双端队列，可以用于实现FIFO类型，也可以用于实现LIFO类型（栈）。
## 数据结构：
ArrayDeque使用数组存储元素，为了满足可以同时在数组两端插入或删除元素的需求，该数组还必须是循环的，即**循环数组（circular array）**，也就是说数组的任何一点都可能被看作起点或者终点。
另外，ArrayDeque的数组为可变数组，数组长度为2的幂次方，每次扩容后长度变为原来的两倍。

![ArrayDeque_base.png](../PNGFigures/ArrayDeque_base.png)

上图中我们看到，**`head`指向首端第一个有效元素，`tail`指向尾端第一个可以插入元素的空位**。因为是循环数组，所以`head`不一定总等于0，`tail`也不一定总是比`head`大。
## 并发
ArrayDeque不是线程安全的。
## 性能
ArrayDeque添加和获取的速度较快，删除操作因为可能涉及到数组元素的移位较为复杂。
## 遍历
ArrayDeque提供了升序迭代器和降序迭代器（获取降序迭代器的方法在Deque接口中定义），可以完成升序遍历和降序遍历。


# 源码分析
ArrayDeque继承自AbstractCollection类，并且实现了Deque，Cloneable和Serializable接口，Cloneable接口则表示可以进行拷贝，Serializable接口表示ArrayDeque可以被序列化。
因为实现了Deque接口，ArrayDeque可以直接操作首部和尾部的元素。
## 成员变量
```java
public class ArrayDeque<E> extends AbstractCollection<E>
                           implements Deque<E>, Cloneable, Serializable{
    transient Object[] elements; //用于存储元素的数组
    transient int head; //指向首端第一个有效元素
    transient int tail; //指向尾端第一个可以插入元素的空位
    private static final int MIN_INITIAL_CAPACITY = 8;//数组的初始容量
}
```
## 构造函数
```java
public class ArrayDeque<E> extends AbstractCollection<E>
                           implements Deque<E>, Cloneable, Serializable{
    //1.无参构造函数
    public ArrayDeque() {
        //数组初始大小为16
        elements = new Object[16];
    }
    
    //2.根据指定的值初始化
    public ArrayDeque(int numElements) {
        allocateElements(numElements);
    }
    
    //3,使用指定集合来初始化
    public ArrayDeque(Collection<? extends E> c) {
        allocateElements(c.size());
        addAll(c);
    }
    
    //初始化数组
    private void allocateElements(int numElements) {
        elements = new Object[calculateSize(numElements)];
    }
    
    //返回大于指定值的最小2次幂数，这一步保证了数组大小肯定为2的幂次方
    private static int calculateSize(int numElements) {
        int initialCapacity = MIN_INITIAL_CAPACITY;
        
        //若给定的数小于8，直接返回8，若给定的数大于8，计算大于给定数的最小2次幂数
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;
            
            //数组容量超过2 ^ 30 
            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }
        return initialCapacity;
    }
}
```
## 重要方法
1.添加
```java
//添加元素到队首
public void addFirst(E e) {
    //不能添加null元素
    if (e == null)
        throw new NullPointerException();
    //将元素保存在head指向的位置的前一位
    elements[head = (head - 1) & (elements.length - 1)] = e;
    if (head == tail)
        //若tail与head相等，说明数组已经被存满，需要扩容
        doubleCapacity();
}

//添加元素到队尾
public void addLast(E e) {
    //不能添加null元素
    if (e == null)
        throw new NullPointerException();
    //将元素保存在tail指向的位置
    elements[tail] = e;
    //将tail加1，若tail与head相等，说明数组已经被存满，需要扩容
    if ( (tail = (tail + 1) & (elements.length - 1)) == head)
        //扩容
        doubleCapacity();
}

//扩容
private void doubleCapacity() {
    assert head == tail;
    int p = head;
    int n = elements.length;
    int r = n - p; // 位于p右侧的元素
    int newCapacity = n << 1;//容量*2
    if (newCapacity < 0)//超过2^30
        throw new IllegalStateException("Sorry, deque too big");
    Object[] a = new Object[newCapacity];
    //将原数组位于head右侧的元素复制到新数组
    System.arraycopy(elements, p, a, 0, r);
    //将原数组位于head左侧的元素复制到新数组
    System.arraycopy(elements, 0, a, r, p);
    elements = a;
    //新数组从0开始存元素
    head = 0;
    tail = n;
}
```
2.返回并移除
```java
//返回并删除首位元素
public E pollFirst() {
    int h = head;
    //队首元素
    E result = (E) elements[h];
    // 队列为空时返回null
    if (result == null)
        return null;
    //将队首置位null
    elements[h] = null; 
    //head向后移一位。  & (elements.length - 1)用来保证循环
    head = (h + 1) & (elements.length - 1);
    return result;
}

//返回并删除末尾元素
public E pollLast() {
    //末尾元素的索引
    int t = (tail - 1) & (elements.length - 1);
    E result = (E) elements[t];
    if (result == null)
        return null;
    //末尾置空
    elements[t] = null;
    tail = t;
    return result;
}

////删除队列中第一个与指定元素相等的元素
public boolean removeFirstOccurrence(Object o) {
    if (o == null)
        return false;
    int mask = elements.length - 1;
    int i = head;
    Object x;
    //升序循环
    while ( (x = elements[i]) != null) {
        //使用元素的equals方法判断元素是否相等
        if (o.equals(x)) {
            //删除元素
            delete(i);
            return true;
        }
        i = (i + 1) & mask;
    }
    return false;
}

////删除队列中最后一个与指定元素相等的元素
public boolean removeLastOccurrence(Object o) {
    if (o == null)
        return false;
    int mask = elements.length - 1;
    int i = (tail - 1) & mask;
    Object x;
    //降序循环
    while ( (x = elements[i]) != null) {
        if (o.equals(x)) {
            //删除元素
            delete(i);
            return true;
        }
        i = (i - 1) & mask;
    }
    return false;
}

//删除指定位置的元素
private boolean delete(int i) {
    checkInvariants();
    final Object[] elements = this.elements;
    final int mask = elements.length - 1;
    final int h = head;
    final int t = tail;
    final int front = (i - h) & mask;
    final int back  = (t - i) & mask;
    
    // 若i到head的长度大于数组有效元素长度，i的位置上没有元素
    if (front >= ((t - h) & mask))
        throw new ConcurrentModificationException();

    // 对删除做了优化，若删除元素更靠近head，删除后前半段的元素后移一位，若删除的元素更靠近tail，删除后后半段元素前移一位。
    if (front < back) {
        //删除元素靠近head
        if (h <= i) {
            //删除元素在head右侧，只许将head到i的元素右移一位
            System.arraycopy(elements, h, elements, h + 1, front);
        } else { // Wrap around
            //删除元素在head左侧，将0到i的元素右移一位，将数组末位元素放到数组首位，再将head~数组末位元素右移一位
            System.arraycopy(elements, 0, elements, 1, i);
            elements[0] = elements[mask];
            System.arraycopy(elements, h, elements, h + 1, mask - h);
        }
        elements[h] = null;
        head = (h + 1) & mask;
        return false;
    } else {
        //删除元素靠近tail
        if (i < t) { 
            //删除元素在tail左侧，只许将i+1到tial的元素右移一位
            System.arraycopy(elements, i + 1, elements, i, back);
            tail = t - 1;
        } else { // Wrap around
            //删除元素在tail右侧，将i+1~数组末位的元素左移一位，将数组首位元素放到数组末位，再将数组首位到tail的元素左移一位
            System.arraycopy(elements, i + 1, elements, i, mask - i);
            elements[mask] = elements[0];
            System.arraycopy(elements, 1, elements, 0, t);
            tail = (t - 1) & mask;
        }
        return true;
    }
}
```
3.获取
```java
public E getFirst() {
    //直接返回head位置的元素
    E result = (E) elements[head];
    if (result == null)
        throw new NoSuchElementException();
    return result;
}

public E getLast() {
    E result = (E) elements[(tail - 1) & (elements.length - 1)];
    if (result == null)
        throw new NoSuchElementException();
    return result;
}

public E peekFirst() {
    return (E) elements[head];
}

public E peekLast() {
    return (E) elements[(tail - 1) & (elements.length - 1)];
}
```

4.堆栈方法
```java
//压栈
public void push(E e) {
    addFirst(e);
}

//弹栈
public E pop() {
    return removeFirst();
}
```
## 迭代器
### DeqIterator
升序迭代器，通过方法iterator()获取。
#### 属性
```java
private class DeqIterator implements Iterator<E> {
    private int cursor = head; //当前光标为head
    private int fence = tail; //结束光标为tail
    private int lastRet = -1; //最后一次获取的元素位置
}
```
#### 方法
1.hasNext()
```java
public boolean hasNext() {
    //当前光标与结束光标相等时，说明已经遍历完
    return cursor != fence;
}
```
2.next()
```java
public E next() {
    if (cursor == fence)
        throw new NoSuchElementException();
    E result = (E) elements[cursor];
    // 快速失败机制，若在迭代时，队列结构发生了修改，抛出异常
    if (tail != fence || result == null)
        throw new ConcurrentModificationException();
    lastRet = cursor;
    //光标后移
    cursor = (cursor + 1) & (elements.length - 1);
    return result;
}
```
3.remove()
```java
public void remove() {
    if (lastRet < 0)
        throw new IllegalStateException();
    //此处需要考虑删除元素时所做的优化情况，若光标之后的元素发生了移位，需要修改光标和fence
    if (delete(lastRet)) {
        //光标回退
        cursor = (cursor - 1) & (elements.length - 1);
        //重置fence
        fence = tail;
    }
    lastRet = -1;
}
```
### DescendingIterator
降序迭代器,通过方法descendingIterator()获取
#### 属性
```java
private class DeqIterator implements Iterator<E> {
    private int cursor = tail; //当前光标为tail
    private int fence = head; //结束光标为head
    private int lastRet = -1; //最后一次获取的元素位置
}
```
#### 方法
1.hasNext()
```java
public boolean hasNext() {
    //当前光标与结束光标相等时，说明已经遍历完
    return cursor != fence;
}
```
2.next()
```java
public E next() {
    if (cursor == fence)
        throw new NoSuchElementException();
    //获取光标前一位的元素
    cursor = (cursor - 1) & (elements.length - 1);
    E result = (E) elements[cursor];
    if (head != fence || result == null)
        throw new ConcurrentModificationException();
    lastRet = cursor;
    return result;
}
```
3.remove()
```java
public void remove() {
    if (lastRet < 0)
        throw new IllegalStateException();
    //此处需要考虑删除的优化情况，若光标之前的元素发生了移位，需要修改光标和fence
    if (!delete(lastRet)) {
        cursor = (cursor + 1) & (elements.length - 1);
        fence = head;
    }
    lastRet = -1;
}
```