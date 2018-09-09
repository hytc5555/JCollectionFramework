# 总体介绍
优先队列是一种特殊的队列，优先队列的作用是能保证每次取出的元素都是队列中权值最小的。这里牵涉到了大小关系，元素大小的评判可以通过元素本身的自然顺序（natural ordering），也可以通过构造时传入的比较器。

## 数据结构：
其通过堆实现，具体说是通过完全二叉树（complete binary tree）实现的小顶堆（任意一个非叶子节点的权值，都不大于其左右子节点的权值），也就意味着可以通过数组来作为PriorityQueue的底层实现。
![PriorityQueue_base.png](../PNGFigures/PriorityQueue_base.png)

上图中我们给每个元素按照层序遍历的方式进行了编号，如果你足够细心，会发现父节点和子节点的编号是有联系的，更确切的说父子节点的编号之间有如下关系：

`leftNo = parentNo*2+1`

`rightNo = parentNo*2+2`

`parentNo = (nodeNo-1)/2`

通过上述三个公式，可以轻易计算出某个节点的父节点以及子节点的下标。这也就是为什么可以直接用数组来存储堆的原因。
## 并发
*PriorityQueue*不是线程安全的。
## 性能
*PriorityQueue*的`peek()`和`element`操作是常数时间，`add()`, `offer()`, 无参数的`remove()`以及`poll()`方法的时间复杂度都是*log(N)*。
## 遍历
PriorityQueue提供了升序迭代器


# 源码分析
PriorityQueue继承自AbstractQueue类，实现的是Queue接口，
## 成员变量
```java
public class PriorityQueue<E> extends AbstractQueue<E>
                              implements java.io.Serializable {
    private static final int DEFAULT_INITIAL_CAPACITY = 11; //数组初始大小
    transient Object[] queue; //存储元素的数组
    private int size = 0; //元素大小
    private final Comparator<? super E> comparator; //使用的比较器
    transient int modCount = 0; //队列结构修改的次数
}
```
## 构造函数
```java
public class PriorityQueue<E> extends AbstractQueue<E>
                              implements java.io.Serializable {
    
    //无参构造函数，数据大小初始化为11，排序使用元素自然顺序
    public PriorityQueue() {
        this(DEFAULT_INITIAL_CAPACITY, null);
    }
    
    //指定数组大小，排序使用元素自然顺序
    public PriorityQueue(int initialCapacity) {
        this(initialCapacity, null);
    }
    
    //指定比较器，数组大小初始化为11
    public PriorityQueue(Comparator<? super E> comparator) {
        this(DEFAULT_INITIAL_CAPACITY, comparator);
    }
    
    //指定数组大小和比较器
    public PriorityQueue(int initialCapacity,
                         Comparator<? super E> comparator) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException();
        this.queue = new Object[initialCapacity];
        this.comparator = comparator;
    }
    
    //使用集合来初始化，若传入的集合是SortedSet或PriorityQueue，使用传入集合的比较器，否则使用元素自然顺序排序
    public PriorityQueue(Collection<? extends E> c) {
        if (c instanceof SortedSet<?>) {
            SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
            this.comparator = (Comparator<? super E>) ss.comparator();
            initElementsFromCollection(ss);
        }
        else if (c instanceof PriorityQueue<?>) {
            PriorityQueue<? extends E> pq = (PriorityQueue<? extends E>) c;
            this.comparator = (Comparator<? super E>) pq.comparator();
            initFromPriorityQueue(pq);
        }
        else {
            this.comparator = null;
            initFromCollection(c);
        }
    }
    
    //使用优先级队列初始化
    public PriorityQueue(PriorityQueue<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
        initFromPriorityQueue(c);
    }
    
    //使用SortedSet集合初始化
    public PriorityQueue(SortedSet<? extends E> c) {
        this.comparator = (Comparator<? super E>) c.comparator();
        initElementsFromCollection(c);
    }
    
}
```
## 重要方法

## 迭代器