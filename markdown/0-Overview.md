# Java集合框架综述

# 介绍
容器，就是可以容纳其他Java对象的对象。*Java Collections Framework（JCF）*为Java开发者提供了通用的容器，其始于JDK 1.2，优点是：

- 降低编程难度
- 提高程序性能
- 提高API间的互操作性
- 降低学习难度
- 降低设计和实现相关API的难度
- 增加程序的重用性

Java容器里只能放对象，对于基本类型（int, long, float, double等），需要将其包装成对象类型后（Integer, Long, Float, Double等）才能放到容器里。很多时候拆包装和解包装能够自动完成。这虽然会导致额外的性能和空间开销，但简化了设计和编程。


# 集合接口
为了规范容器的行为，统一设计，JCF定义了14种容器接口（collection interfaces）。它们的关系如下图所示：<br>
*Map*接口没有继承自*Collection*接口，因为*Map*表示的是关联式容器而不是集合。但Java为我们提供了从*Map*转换到*Collection*的方法，可以方便的将*Map*切换到集合视图。
![pic](../PNGFigures/JCF_Collection_Interfaces.png)

# 集合实现
上述接口的通用实现见下表：
<table align="center"><tr><td colspan="2" rowspan="2" align="center" border="0"></td><th colspan="5" align="center">Implementations</th></tr><tr><th>Hash Table</th><th>Resizable Array</th><th>Balanced Tree</th><th>Linked List</th><th>Hash Table + Linked List</th></tr><tr><th rowspan="4">Interfaces</th><th>Set</th><td><tt>HashSet</tt></td><td></td><td><tt>TreeSet</tt></td><td></td><td><tt>LinkedHashSet</tt></td></tr><tr><th>List</th><td></td><td><tt>ArrayList</tt></td><td></td><td><tt>LinkedList</tt></td><td></td></tr><tr><th>Deque</th><td></td><td><tt>ArrayDeque</tt></td><td></td><td><tt>LinkedList</tt></td><td></td></tr><tr><th>Map</th><td><tt>HashMap</tt></td><td></td><td><tt>TreeMap</tt></td><td></td><td><tt>LinkedHashMap</tt></td></tr></table>


# 接口规范

## java.util.Collection
集合　Since1.2<br>
集合中的根接口，定义集合的基本操作。

方法详情：
```java
public interface Collection<E> extends Iterable<E> {
    
    boolean add(E e); //添加元素
    
    boolean addAll(Collection<? extends E> c); //将指定集合中的所有元素添加到此集合中
    
    void clear(); //从此集合中删除所有元素
    
    boolean contains(Object o); //如果此collection包含指定的元素，则返回true
    
    boolean containsAll(Collection<?> c); //如果此collection包含指定collection中的所有元素，则返回true。
    
    boolean equals(Object o); //将指定对象与此集合进行比较判断是否相等
    
    int hashCode(); //返回此集合的哈希码值
    
    boolean isEmpty(); //如果此collection不包含任何元素，则返回true。
    
    Iterator<E> iterator(); //返回此集合中迭代器。
    
    boolean remove(Object o); //从该集合中删除指定元素的第一个匹配项（如果存在）
    
    boolean removeAll(Collection<?> c); //删除此集合的所有元素，这些元素也包含在指定的集合中。
    
    boolean retainAll(Collection<?> c); //仅保留此集合中包含在指定集合中的元素
    
    int size(); //返回此集合中的元素数
    
    Object[] toArray(); //返回包含此集合中所有元素的数组
    
    <T> T[] toArray(T[] a); //返回一个包含此collection中所有元素的数组;返回数组的运行时类型是指定数组的运行时类型
    
    //1.8新增接口
    
    default boolean removeIf(Predicate<? super E> filter) {} // 删除此集合中满足给定条件的所有元素。
    
    default Spliterator<E> spliterator() {} // 返回此集合中可并行的迭代器
    
    default Stream<E> stream() {} //返回以此集合为源的Stream
    
    default Stream<E> parallelStream() {} //以此集合作为源返回可以并行的Stream。
}
```

### Queue 
队列 Since1.5<br>
　　队列继承自Collection，除了基本的集合操作外，队列还提供额外的插入、删除和检索的方法。这些方法都以两种形式存在：一种在操作失败时抛出异常，另一种返回特殊值（null或false，具体取决于操作）。<br>
　　队列通常（但不一定）以FIFO（先进先出）方式对元素进行排序,在FIFO队列中，所有新元素都插入队列的尾部。 其中的例外是优先级队列和以及LIFO队列（或堆栈）,优先级队列它根据提供的比较器或者元素的自然顺序对元素进行排序；栈对元素以LIFO（后进先出）的方式进行排序。<br>
　　队列实现通常不允许插入null元素，尽管某些实现（如LinkedList）不禁止插入null。，但也不应将null插入到队列中，因为null也被poll方法用作特殊返回值，以指示队列不包含任何元素。<br>

方法摘要:<br>
<table align="center"> <tr> <td></td> <td>Throws exception</td> <td>Returns special value</td> </tr> <tr> <th>Insert</th> <td>add(e)</td> <td>offer(e)</td> </tr> <tr> <th>Remove</th> <td>remove()</td> <td>poll()</td> </tr> <tr> <th>Examine</th> <td>element()</td> <td>peek()</td> </tr> </table>
方法详情：<br><br>

```java
public interface Queue<E> extends Collection<E> {
    
    //Throws exception
    
    boolean add(E e); //若队列容量未达到限制，则将元素插入队尾，成功返回true，如果当前没有可用空间则抛出IllegalStateException
    
    E remove(); //返回并删除队首元素，如果此队列为空，抛出NoSuchElementException异常
    
    E element(); //返回队首元素但不删除，如果此队列为空，则抛出异常。
    
    
    //Returns special value
    
    boolean offer(E e); //若队列容量未达到限制，则将元素插入队尾，成功返回true，如果当前没有可用空间则返回false。
    
    E poll(); //返回并删除队首元素，如果此队列为空，则返回null
    
    E peek(); //返回队首元素但不删除，如果此队列为空，则返回null。
}
```

## BlockingQueue
阻塞队列，since 1.5<br>
    阻塞队列在Queue的基础上增加了两种形式的方法，一种是会无限期地阻塞当前线程直到操作成功，另一种在超出时间限制后抛出异常。阻塞形式方法支持在获取元素时等待队列变为非空的操作，支持在添加元素时等待队列中的空间可用。<br>
　　BlockingQueue的实现是线程安全的，实现主要用于生产者 - 消费者队列,可以有多个生产者和多个消费者。<br>

方法摘要:<br>
<table align="center"> <tr> <td></td> <td>Throws exception</td> <td>Special value</td> <td>Blocks</td> <td>Times out</td> </tr> <tr> <th>Insert</th> <td>add(e)</td> <td>offer(e)</td> <td>put(e)</td> <td>offer(e, time, unit)</td> </tr> <tr> <th>Remove</th> <td>remove()</td> <td>poll()</td> <td>take()</td> <td>poll(time, unit)</td> </tr> <tr> <th>Examine</th> <td>element()</td> <td>peek()</td> <td>not applicable</td> <td>not applicable</td> </tr> </table>
方法详情:<br><br>

```java
public interface BlockingQueue<E> extends Queue<E> {
    
    //Throws exception
    
    boolean add(E e); //若队列容量未达到限制，则将元素插入队尾，成功返回true，如果当前没有可用空间则抛出IllegalStateException
    
    E remove(); //返回并删除队首元素，如果此队列为空，抛出NoSuchElementException异常
    
    E element(); //返回队首元素但不删除，如果此队列为空，则抛出异常。
    
    
    //Returns special value
    
    boolean offer(E e); //若队列容量未达到限制，则将元素插入队尾，成功返回true，如果当前没有可用空间则返回false。
    
    E poll(); //返回并删除队首元素，如果此队列为空，则返回null
    
    E peek(); //返回队首元素但不删除，如果此队列为空，则返回null。
    
    
    //Blocks
    
    void put(E e) throws InterruptedException; //将指定的元素插入队尾，若此时队列已满，阻塞线程，等待队列空间变为可用
    
    E take() throws InterruptedException; //返回并删除队首元素，若此时队列为空，阻塞线程，等待队列空间中被放入元素
    
    
    //Time out
    
    boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException; //将指定的元素插入队尾，若此时队列已满，阻塞线程，在指定的时间内等待队列空间变为可用
                                                                                //如果成功则返回true;如果等待超过了指定的等待时间，则返回false
   
    E poll(long timeout, TimeUnit unit) throws InterruptedException; //返回并删除队首元素，若此时队列为空，阻塞线程，在指定的时间内等待队列空间中被放入元素
                                                                     //如果成功则返回队首元素;如果等待超过了指定的等待时间，则返回null
    
    int remainingCapacity(); //返回理想情况下（在没有内存或资源约束的情况下）此队列可以无阻塞地接受的其他元素的数量，如果没有内部限制，则返回Integer.MAX_VALUE
    
    int drainTo(Collection<? super E> c); //从此队列中删除所有可用元素，并将它们添加到给定集合中
    
    int drainTo(Collection<? super E> c, int maxElements); //从此队列中删除所有可用元素，并将它们添加到给定集合中,可以指定最大转移元素数量
}
```

## TransferQueue
传输队列，since1.7

　传输队列继承自BlockingQueue，在BlockingQueue中，生产者往队列中添加元素(put方法)，若队列已满，会进入阻塞状态，直到队列中有可用空间。而TransferQueue更进一步，生产者会一直阻塞直到所添加到队列的元素被某一个消费者所消费。

方法详情:<br>
```java
public interface TransferQueue<E> extends BlockingQueue<E> {
    
    void transfer(E e) throws InterruptedException; //若当前存在一个正在等待获取的消费者线程，即立刻将e移交之；否则将元素e插入到队列尾部，并且当前线程进入阻塞状态，直到有消费者线程取走该元素
    
    boolean tryTransfer(E e); //若当前存在一个正在等待获取的消费者线程，则该方法会即刻转移e，并返回true;若不存在则返回false，但是并不会将e插入到队列中。这个方法不会阻塞当前线程，要么快速返回true，要么快速返回false。
    
    boolean tryTransfer(E e, long timeout, TimeUnit unit) throws InterruptedException; //若当前存在一个正在等待获取的消费者线程，会立即传输给它; 否则将元素e插入到队列尾部，并且等待被消费者线程获取消费掉。若在指定的时间内元素e无法被消费者线程获取，则返回false，同时该元素从队列中移除。
    
    boolean hasWaitingConsumer(); //如果至少有一个等待消费者，则为true
    
    int getWaitingConsumerCount(); //等待接收元素的消费者数量
}
```

## Deque
双端队列 since1.6

　　继承自队列，在队列基础上增加了直接对队首和队尾操作的方法，通过增加的方法可以实现FIFO,LIFO（栈）两种数据结构。

方法摘要:<br>
<table align="center"> <tr> <td></td> <th colspan="2">First Element (Head)</th> <th colspan="2">Last Element (Tail)</th> </tr> <tr> <td></td> <td>Throws exception</td> <td>Special value</td> <td>Throws exception</td> <td>Special value</td> </tr> <tr> <th>Insert</th> <td>addFirst(e)</td> <td>offerFirst(e)</td> <td>addLast(e)</td> <td>offerLast(e)</td> </tr> <tr> <th>Remove</th> <td>removeFirst()</td> <td>pollFirst()</td> <td>removeLst()</td> <td>pollLast()</td> </tr> <tr> <th>Examine</th> <td>getFirst()</td> <td>peekFirst()</td> <td>getLast()</td> <td>peekLast()</td> </tr> </table>


方法详情:<br>
```java
public interface Deque<E> extends Queue<E> {
    
    //Throws exception
    
    boolean add(E e); //若队列容量未达到限制，则将元素插入队尾，返回true，如果当前没有可用空间则抛出IllegalStateException
    void addFirst(E e); //若队列容量未达到限制，则将元素插入队首，返回true，如果当前没有可用空间则抛出IllegalStateException
    void addLast(E e); //若队列容量未达到限制，则将元素插入队尾，返回true，如果当前没有可用空间则抛出IllegalStateException
    
    E remove(); //返回并删除队首元素，如果此队列为空，抛出NoSuchElementException异常
    E removeFirst(); //返回并删除队首元素，如果此队列为空，抛出NoSuchElementException异常
    E removeLast(); //返回并删除队首元素，如果此队列为空，抛出NoSuchElementException异常
    
    E element(); //返回队首元素但不删除，如果此队列为空，则抛出异常
    E getFirst(); //返回队首元素但不删除，如果此队列为空，则抛出异常
    E getLast(); //返回队尾元素但不删除，如果此队列为空，则抛出异常
    
    
    //Returns special value
    
    boolean offer(E e); //若队列容量未达到限制，则将元素插入队尾，成功返回true，如果当前没有可用空间则返回false。
    boolean offerFirst(E e); //若队列容量未达到限制，则将元素插入队首，成功返回true，如果当前没有可用空间则返回false。
    boolean offerLast(E e); //若队列容量未达到限制，则将元素插入队尾，成功返回true，如果当前没有可用空间则返回false。
    
    E poll(); //返回并删除队首元素，如果此队列为空，则返回null
    E pollFirst(); //返回并删除队首元素，如果此队列为空，则返回null
    E pollLast(); //返回并删除队尾元素，如果此队列为空，则返回null
    
    E peek(); //返回队首元素但不删除，如果此队列为空，则返回null。
    E peekFirst(); //返回队首元素但不删除，如果此队列为空，则返回null。
    E peekLast(); //返回队尾元素但不删除，如果此队列为空，则返回null。
    
    //Stack
    
    void push(E e);//向栈顶插入元素，如果队列已满，则抛出IllegalStateException异常
    
    E pop();//删除并返回此双端队列的第一个元素，如果队列为空，则抛出NoSuchElementException异常
    
    
    boolean removeFirstOccurrence(Object o); //删除队列中第一个与指定元素相等的元素

    boolean removeLastOccurrence(Object o); //删除队列中最后一个与指定元素相等的元素
    
    Iterator<E> descendingIterator(); //获取队列的倒序迭代器
}
```

## BlockingDeque
阻塞双端队列 since1.6

　　阻塞双端队列继承自阻塞队列，增加了直接操作队首或队尾元素的方法
  　
  
方法摘要:<br>
<table align="center"> <tr><th colspan="5">First  Element (Head)</th></tr> <tr> <td></td> <td>Throws exception</td> <td>Special value</td> <td>Blocks</td> <td>Times out</td> </tr> <tr> <th>Insert</th> <td>addFirst(e)</td> <td>offerFirst(e)</td> <td>putFirst(e)</td> <td>offerFirst(e, time, unit)</td> </tr> <tr> <th>Remove</th> <td>removeFirst()</td> <td>pollFirst()</td> <td>takeFirst()</td> <td>pollFirst(time, unit)</td> </tr> <tr> <th>Examine</th> <td>getFirst()</td> <td>peekFirst()</td> <td>not applicable</td> <td>not applicable</td> </tr> <tr><th colspan="5">Last Element (Tail)</th></tr> <tr> <td></td> <td>Throws exception</td> <td>Special value</td> <td>Blocks</td> <td>Times out</td> </tr> <tr> <th>Insert</th> <td>addLast(e)</td> <td>offerLast(e)</td> <td>putLast(e)</td> <td>offerLast(e, time, unit)</td> </tr> <tr> <th>Remove</th> <td>removeLast()</td> <td>pollLast()</td> <td>takeLast()</td> <td>pollLast(time, unit)</td> </tr> <tr> <th>Examine</th> <td>getLast()</td> <td>peekLast()</td> <td>not applicable</td> <td>not applicable</td> </tr> </table>

方法详情:<br>

```java
public interface BlockingDeque<E> extends BlockingQueue<E>, Deque<E> {
    
    //Throws exception
    
    boolean add(E e); //若队列容量未达到限制，则将元素插入队尾，返回true，如果当前没有可用空间则抛出IllegalStateException
    void addFirst(E e); //若队列容量未达到限制，则将元素插入队首，返回true，如果当前没有可用空间则抛出IllegalStateException
    void addLast(E e); //若队列容量未达到限制，则将元素插入队尾，返回true，如果当前没有可用空间则抛出IllegalStateException
    
    E remove(); //返回并删除队首元素，如果此队列为空，抛出NoSuchElementException异常
    E removeFirst(); //返回并删除队首元素，如果此队列为空，抛出NoSuchElementException异常
    E removeLast(); //返回并删除队首元素，如果此队列为空，抛出NoSuchElementException异常
    
    E element(); //返回队首元素但不删除，如果此队列为空，则抛出异常
    E getFirst(); //返回队首元素但不删除，如果此队列为空，则抛出异常
    E getLast(); //返回队尾元素但不删除，如果此队列为空，则抛出异常
    
    
    //Returns special value
    
    boolean offer(E e); //若队列容量未达到限制，则将元素插入队尾，成功返回true，如果当前没有可用空间则返回false。
    boolean offerFirst(E e); //若队列容量未达到限制，则将元素插入队首，成功返回true，如果当前没有可用空间则返回false。
    boolean offerLast(E e); //若队列容量未达到限制，则将元素插入队尾，成功返回true，如果当前没有可用空间则返回false。
    
    E poll(); //返回并删除队首元素，如果此队列为空，则返回null
    E pollFirst(); //返回并删除队首元素，如果此队列为空，则返回null
    E pollLast(); //返回并删除队尾元素，如果此队列为空，则返回null
    
    E peek(); //返回队首元素但不删除，如果此队列为空，则返回null。
    E peekFirst(); //返回队首元素但不删除，如果此队列为空，则返回null。
    E peekLast(); //返回队尾元素但不删除，如果此队列为空，则返回null。
    
    
    //Blocks
    
    void put(E e) throws InterruptedException; //将指定的元素插入队尾，若此时队列已满，阻塞线程，等待队列空间变为可用
    void putFirst(E e) throws InterruptedException;  //将指定的元素插入队首，若此时队列已满，阻塞线程，等待队列空间变为可用
    void putLast(E e) throws InterruptedException; //将指定的元素插入队尾，若此时队列已满，阻塞线程，等待队列空间变为可用
    
    E take() throws InterruptedException; //返回并删除队首元素，若此时队列为空，阻塞线程，等待队列空间中被放入元素
    E takeFirst() throws InterruptedException; //返回并删除队首元素，若此时队列为空，阻塞线程，等待队列空间中被放入元素
    E takeLast() throws InterruptedException; //返回并删除队尾元素，若此时队列为空，阻塞线程，等待队列空间中被放入元素
    
    
    //Time out
    
    boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException; //将指定的元素插入队尾，若此时队列已满，阻塞线程，在指定的时间内等待队列空间变为可用
                                                                                //如果成功则返回true;如果等待超过了指定的等待时间，则返回false
    boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException;//将指定的元素插入队首，若此时队列已满，阻塞线程，在指定的时间内等待队列空间变为可用
                                                                                     //如果成功则返回true;如果等待超过了指定的等待时间，则返回false
    boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException;//将指定的元素插入队尾，若此时队列已满，阻塞线程，在指定的时间内等待队列空间变为可用
                                                                                     //如果成功则返回true;如果等待超过了指定的等待时间，则返回false

    E poll(long timeout, TimeUnit unit) throws InterruptedException; //返回并删除队首元素，若此时队列为空，阻塞线程，在指定的时间内等待队列空间中被放入元素
                                                                     //如果成功则返回队首元素;如果等待超过了指定的等待时间，则返回null    
    E pollFirst(long timeout, TimeUnit unit) throws InterruptedException; //返回并删除队首元素，若此时队列为空，阻塞线程，在指定的时间内等待队列空间中被放入元素
                                                                          //如果成功则返回队首元素;如果等待超过了指定的等待时间，则返回null
    E pollLast(long timeout, TimeUnit unit) throws InterruptedException; //返回并删除队尾元素，若此时队列为空，阻塞线程，在指定的时间内等待队列空间中被放入元素
                                                                          //如果成功则返回队尾元素;如果等待超过了指定的等待时间，则返回null
}
```

## List
列表 since1.2
　　列表是有序的集合，，实现该接口后可以精确控制每个元素的插入位置。可以通过索引（列表中的位置）访问元素，并搜索列表中的元素。
List接口提供了一个特殊的迭代器，称为ListIterator，它允许在便利过程中进行元素插入和替换，以及Iterator接口提供的常规操作之外的双向访问。该迭代器还可以指定遍历开始的位置。

方法详情:<br>

```java
public interface List<E> extends Collection<E> {
    
    //省略 Method from Collection
    
    boolean add(E e); //将指定的元素追加到此列表的末尾
    
    void add(int index, E element); //将指定元素插入此列表中的指定位置
    
    boolean addAll(int index, Collection<? extends E> c); //将将指定集合中的所有元素插入此列表中的指定位置
    
    E remove(int index);//从该列表中删除指定元素的第一个匹配项（如果存在）
    
    E get(int index); //返回此列表中指定位置的元素
    
    int indexOf(Object o); //返回此列表中第一次出现的指定元素的索引，如果此列表不包含该元素，则返回-1。
    
    int lastIndexOf(Object o);//返回此列表中指定元素最后一次出现的索引，如果此列表不包含该元素，则返回-1。
    
    ListIterator<E> listIterator();//返回此列表中元素的列表迭代器,除了Iterator接口的功能外，还支持双向迭代，元素替换，元素插入和索引检索
    
    ListIterator<E> listIterator(int index);//从列表中的指定位置开始，返回列表中元素的列表迭代器，除了Iterator接口的功能外，还支持双向迭代，元素替换，元素插入和索引检索
    
    List<E> subList(int fromIndex, int toIndex);//返回指定开始和结束位置的视图
    
    
    //1.8新增接口 
    default void replaceAll(UnaryOperator<E> operator) {} // 对每个元素执行operator指定的操作，并用操作结果来替换原来的元素。
    
    default void sort(Comparator<? super E> c) {} //根据c指定的比较规则对容器元素进行排序
}
```

## Set
无重复元素的集合 since1.2
　　
方法详情:<br>
```java
public interface Set<E> extends Collection<E> {
    //Method from Collection
}
```
## SortedSet
元素支持排序，需要元素实现Comparable接口，或向Set传入Comparator
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



