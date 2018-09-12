https://github.com/CarpenterLee/JCFInternals/edit/master/markdown/6-HashSet%20and%20HashMap.md

# 总体介绍
*HashSet*和*HashMap*在Java里有着相同的实现，前者仅仅是对后者做了一层包装，也就是说***HashSet*里面有一个*HashMap*（适配器模式）**。

*HashMap*实现了*Map*接口，即允许放入`key`为`null`的元素，也允许插入`value`为`null`的元素。<br>

`HashMap`除了未实现同步外，其余跟`Hashtable`大致相同；
## 数据结构
HashMap使用hash表存储元素，跟*TreeMap*不同，该容器不保证元素顺序，根据需要该容器可能会对元素重新哈希，元素的顺序也会被重新打散，因此不同时间迭代同一个*HashMap*的顺序可能会不同。
根据对冲突的处理方式不同，哈希表有两种实现方式，一种开放地址方式（Open addressing），另一种是冲突链表方式（Separate chaining with linked lists）。**Java *HashMap*采用的是冲突链表方式**。
![HashMap_base](../../PNGFigures/HashMap_base.png)

## 并发
HashSet和HashMap为线程不同步

## 性能
从上图容易看出，如果选择合适的哈希函数，`put()`和`get()`方法可以在常数时间内完成。但在对*HashMap*进行迭代时，需要遍历整个table以及后面跟的冲突链表。因此对于迭代比较频繁的场景，不宜将*HashMap*的初始大小设的过大。
有两个参数可以影响*HashMap*的性能：初始容量（inital capacity）和负载系数（load factor）。初始容量指定了初始`table`的大小，负载系数用来指定自动扩容的临界值。当`entry`的数量超过`capacity*load_factor`时，容器将自动扩容并重新哈希。对于插入元素较多的场景，将初始容量设大可以减少重新哈希的次数。

将对象放入到*HashMap*或*HashSet*中时，有两个方法需要特别关心：`hashCode()`和`equals()`。**`hashCode()`方法决定了对象会被放到哪个`bucket`里，当多个对象的哈希值冲突时，`equals()`方法决定了这些对象是否是“同一个对象”**。所以，如果要将自定义的对象放入到`HashMap`或`HashSet`中，需要*@Override*`hashCode()`和`equals()`方法。
## 遍历
提供key的视图，value的视图和entry的视图，可以分别对key、value和entry进行遍历。

# 源码分析
## 成员变量
```java
public class HashMap<K,V> extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable {
    
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;//默认初始化大小
    static final int MAXIMUM_CAPACITY = 1 << 30;//最大大小2^30
    static final float DEFAULT_LOAD_FACTOR = 0.75f;//默认的填充因子
    static final int TREEIFY_THRESHOLD = 8; //添加时若一个bucket的元素个数大于TREEIFY_THRESHOLD，将存储结构改为红黑树
    static final int UNTREEIFY_THRESHOLD = 6;
    static final int MIN_TREEIFY_CAPACITY = 64;
    
    transient Node<K,V>[] table; //存储元素的数组
    transient Set<Map.Entry<K,V>> entrySet; //映射视图
    transient int size; //元素大小
    transient int modCount; //结构修改次数
    int threshold; //临界值
    final float loadFactor; //填充因子
    
    //用于存储元素的对象
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash; //hash值
        final K key; 
        V value; 
        Node<K,V> next; //下一个元素
        
        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
        
        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }
    }
}
```
## 重要方法
### get()
get(Object key)方法根据指定的key值返回对应的value,从代码中可以看到，add方法调用了getNode(hash,key)方法来获取entry。
思想是首先通过`hash()`函数得到对应`bucket`的下标，然后依次遍历冲突链表，通过`key.equals(k)`方法来判断是否是要找的那个`entry`。
![HashMap_getEntry](../../PNGFigures/HashMap_getEntry.png)
上图中`hash(k)&(table.length-1)`等价于`hash(k)%table.length`，原因是*HashMap*要求`table.length`必须是2的指数，因此`table.length-1`就是二进制低位全是1，跟`hash(k)`相与会将哈希值的高位全抹掉，剩下的就是余数了。<br>

代码如下
```java
final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    //上文提到的计算index的方法：(n - 1) & hash，first是这个数组table中index下标处存放的对象
    if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && // always check first node
            //如果first对象匹配成功，则直接返回
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            //否则就要在index指向的链表或红黑树（如果有的话）中进行查找
            if (first instanceof TreeNode)
                //如果first节点是TreeNode对象，则说明存在的是红黑树结构，这是我们今天要关注的重点
                return ((TreeNode<K,V>)first).getTreeNode(hash, key); 
            //否则的话就是一个普通的链表，则从头节点开始遍历查找
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    return null;
}

/**
* Calls find for root node.
*/
//定位到树的根节点，并调用其find方法
final TreeNode<K,V> getTreeNode(int h, Object k) {
    return ((parent != null) ? root() : this).find(h, k, null);
}

final TreeNode<K,V> find(int h, Object k, Class<?> kc) {
    TreeNode<K,V> p = this; //p赋值为根节点，并从根节点开始遍历
    do {
        int ph, dir; K pk;
        TreeNode<K,V> pl = p.left, pr = p.right, q;
        if ((ph = p.hash) > h) //查找的hash值h比当前节点p的hash值ph小
            p = pl; //在p的左子树中继续查找
        else if (ph < h)
            p = pr; //反之在p的右子树中继续查找
        else if ((pk = p.key) == k || (k != null && k.equals(pk)))
            return p; //若两节点hash值相等，且节点的key也相等，则匹配成功，返回p

    /****---- 下面的情况是节点p的hash值和h相等，但key不匹配，需继续在p的子树中寻找 ----****/

        else if (pl == null)
            p = pr; //若p的左子树为空，则直接在右子树寻找。若右子树也为空，则会不满足循环条件，返回null，即未找到
        else if (pr == null)
            p = pl; //反之若左子树不为空，同时右子树为空，则继续在左子树中寻找
        else if ((kc != null || (kc = comparableClassFor(k)) != null) &&
                     (dir = compareComparables(kc, k, pk)) != 0)
            //若k的比较函数kc不为空，且k是可比较的，则根据k和pk的比较结果来决定继续在p的哪个子树中寻找
            p = (dir < 0) ? pl : pr;
        //若k不可比，则只能分别去p的左右子树中碰运气了，先在p的右子树pr中寻找，结果为q
        else if ((q = pr.find(h, k, kc)) != null)
            return q; //若q不为空，代表匹配成功，则返回q，结束
        else
            p = pl; //到这里表示未能在p的右子树中匹配成功，则在左子树中继续
    } while (p != null);
    //各种寻找均无果，返回null，表示查找失败。
    return null;
}
```
### hash()
计算元素hash码值，若key是null,hash码取0，所以null键保存在数组第一位。
先通过key.hashCode()获取到元素的hashCode，将hashCode向右移16位，高位补0，再与远hashCode做异或运算，得到的结果是，hashCode高16位不变，低16位与原高16位做异或运算。
```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```
### put()
put()方法内部调用了putVal()把映射存入hash表。

```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length; //若当前哈希数组table的长度为0，则进行扩容
    //确定输入的hash在哈希数组中对应的下标i
    if ((p = tab[i = (n - 1) & hash]) == null)
        //若数组该位置之前没有被占用，则新建一个节点放入，插入完成。
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            //若该位置上存放的第一个节点p能与输入的节点信息匹配，则将p记录为e并结束查找
            e = p;
        else if (p instanceof TreeNode)
            //若该位置的第一个节点p为TreeNode类型，说明这里存放的是一棵红黑树，p为根节点。
            //于是交给putTreeVal方法来完成后续操作，该方法下文会有详述
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            //走到这里，说明p不匹配且是一个链表的头结点，该遍历链表了
            for (int binCount = 0; ; ++binCount) {
                //e指向p的下一个节点
                if ((e = p.next) == null) {
                    //若e为空，则说明已经到表尾了还未能匹配，则在表尾处插入新节点
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        //若插入后，该桶中的节点个数已达到了树化阈值
                        //则对该桶进行树化。该部分源码下文会有详述
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    //匹配成功，我们需要用新的value来覆盖e节点
                    break;

                p = e; //循环继续
            }
        }
        //若执行到此时e不为空，则说明在map中找到了与key相匹配的节点e
        if (e != null) { // existing mapping for key
            V oldValue = e.value; //暂存e节点当前的值为oldValue
            if (!onlyIfAbsent || oldValue == null)
                //若onlyIfAbsent==true，则已存在节点的value不能被覆盖，除非其value为null
                //否则的话，用输入的value覆盖e.value
                e.value = value;
            //钩子方法，这在HashMap中是个空方法，但是在其子类LinkedHashMap中会被Override
            //通知子类：节点e被访问过了
            afterNodeAccess(e);
            //返回已被覆盖的节点e的oldValue
            return oldValue;
        }
    }

    /****--执行到此处说明没有匹配到已存在节点，一定是有新节点插入--****/

    ++modCount; //结构操作数加一
    if (++size > threshold)
        resize(); //插入后，map中的节点数加一，若此时已达阈值，则扩容
    afterNodeInsertion(evict); //同样的钩子方法，通知子类有新节点插入
    return null; //由于是新节点插入，没有节点被覆盖，故返回null
}
```
通过上面的代码可以清楚的看到插入操作的整体流程：

a. 先通过key的hash定位到table数组中的一个桶位;

b. 若此桶没有被占用，则新建节点，占坑，记录，考虑扩容，结束。若已被占用，则总是先与第一个节点进行一次匹配，若成功则无需后续的遍历操作，直接覆盖；否则的话需进行遍历；

c. 若桶中的第一个节点p是TreeNode类型，则表示桶中存在的是一棵红黑树，于是后续操作将由putTreeVal方法来完成。否则的话说明桶中的是一个链表，则对该链表进行遍历；

d. 若遍历过程中匹配到了节点e，则进行覆盖。否则的话通过遍历定位到合适的插入位置，新建节点插入，对于链表结构需考虑是否树化。最后进行操作记录，考虑扩容，结束。

```java
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index; Node<K,V> e;
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
        resize(); //若table数组为空或其容量小于最小树化值，则用扩容取代树化
    else if ((e = tab[index = (n - 1) & hash]) != null) { //定位到hash对应的桶位，头结点记为e
        TreeNode<K,V> hd = null, tl = null; //声明两个指针分别指向链表头尾节点
        do {
            TreeNode<K,V> p = replacementTreeNode(e, null); //将Node类型的节点e替换为TreeNode类型的p
            if (tl == null)
                hd = p; //若当前链表为空，则赋值头指针为p
            else {
                p.prev = tl; //否则将p添加到链表尾部
                tl.next = p;
            }
            tl = p; //后移尾指针
        } while ((e = e.next) != null); //循环继续

        if ((tab[index] = hd) != null) //将链表头节点放入table的index位置
            hd.treeify(tab); //通过treeify方法将链表树化
    }
}

final void treeify(Node<K,V>[] tab) {
    TreeNode<K,V> root = null; //声明root变量以记录根节点
    for (TreeNode<K,V> x = this, next; x != null; x = next) { //从调用节点this开始遍历
        next = (TreeNode<K,V>)x.next; //暂存链表中的下一个节点，记为next
        x.left = x.right = null; //当前节点x的左右子树置空
        if (root == null) {
            x.parent = null; //若root仍为空，则将x节点作为根节点
            x.red = false; //红黑树特性之一：根节点为黑色
            root = x; //赋值root
        }
        else { //否则的话需将当前节点x插入到已有的树中
            K k = x.key;
            int h = x.hash;
            Class<?> kc = null;
            //第二层循环，从根节点开始寻找适合x插入的位置，并完成插入操作。
            //putTreeVal方法的实现跟这里十分相似。
            for (TreeNode<K,V> p = root;;) { 
                int dir, ph;
                K pk = p.key;
                if ((ph = p.hash) > h) //若x的hash值小于节点p的，则往p的左子树中继续寻找
                    dir = -1;
                else if (ph < h) //反之在右子树中继续
                    dir = 1;
                //若两节点hash值相等，且key不可比，则利用System.identityHashCode方法来决定一个方向
                else if ((kc == null && (kc = comparableClassFor(k)) == null) ||
                        (dir = compareComparables(kc, k, pk)) == 0)
                   dir = tieBreakOrder(k, pk); 

                TreeNode<K,V> xp = p; //将当前节点p暂存为xp
                //根据上面算出的dir值将p向下移向其左子树或右子树，若为空，则说明找到了合适的插入位置，否则继续循环
                if ((p = (dir <= 0) ? p.left : p.right) == null) { 
                    //执行到这里说明找到了合适x的插入位置
                    x.parent = xp; //将x的parent指针指向xp
                    if (dir <= 0) //根据dir决定x是作为xp的左孩子还是右孩子
                        xp.left = x;
                    else
                        xp.right = x;
                    //由于需要维持红黑树的平衡，即始终满足其5条性质，每一次插入新节点后都需要做平衡操作
                    //这个方法的源码我们在<<红黑树(Red-Black Tree)解析>>一文中已有详细分析，此处不再重复
                    root = balanceInsertion(root, x);
                    break; //插入完成，跳出循环
                }
            }
        }
    }
    //由于插入后的平衡调整可能会更换整棵树的根节点，
    //这里需要通过moveRootToFront方法确保table[index]中的节点与插入前相同
    moveRootToFront(tab, root);
}
```

### resize()

在扩容时会有4种情况<br>
1. 数组已经初始化,但数组容量已达上限，直接返回原数组，将临界值改为Integer.MAX_VALUE，后续不会在触发resize()
2. 数组已经初始化,数组容量大于16，且扩容为两倍后依然小于容量上限，则扩容两倍，newCap = oldCap * 2，newThr = oldThr * 2
3. 数组没有初始化，已经在构造函数初始化threshold，newCap = threshold，newThr=0
4. 数组没有初始化，没有在构造函数初始化threshold，newCap = DEFAULT_INITIAL_CAPACITY，newThr=(int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY) 
 
```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length; //定义老数组大小
    int oldThr = threshold; //定义老数组临界值
    int newCap, newThr = 0;//定义新数组大小和新数组临界值
    
    //表示数组已经完成初始化
    if (oldCap > 0) { 
        //情况1 
        if (oldCap >= MAXIMUM_CAPACITY) { 
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        //情况2 
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&   //newCap = oldCap * 2
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; //newThr = oldThr * 2
    }
    // 数组未初始化
    else if (oldThr > 0) // 情况3 
        newCap = oldThr;
    else { // 情况4 
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) { //情况3 需要重新计算临界值
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    //建立新数组
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) { //若原数组不为空，需要将原数组的元素转移到新数组
        for (int j = 0; j < oldCap; ++j) { //遍历原数组
            Node<K,V> e;
            if ((e = oldTab[j]) != null) { //数组在j位置上有元素
                oldTab[j] = null;
                if (e.next == null) //数组在j位置上只有一个元素，重新计算元素对应`bucket`的下标
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode) //有hash冲突，存储方式为红黑树
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // 有hash冲突，存储方式为链表
                    Node<K,V> loHead = null, loTail = null; //低位元素
                    Node<K,V> hiHead = null, hiTail = null; //高位元素
                    Node<K,V> next;
                    do {
                        next = e.next;
                        //oldCap为2的幂次方
                        if ((e.hash & oldCap) == 0) { //说明hash值的高位为0，hash&(newCap-1) == hash&(oldCap-1)，元素位置不变
                            if (loTail == null)
                                loHead = e; //低位链表的头部定义为e
                            else
                                loTail.next = e;
                            loTail = e; //低位链表的尾部定义为e
                        }
                        else { //说明hash值高位为1，hash&(newCap-1) != hash&(oldCap-1)
                            if (hiTail == null)
                                hiHead = e; //高位链表的头部定义为e
                            else
                                hiTail.next = e;
                            hiTail = e; //高位链表的头部定义为e
                        }
                    } while ((e = next) != null);
                    //循环结束后，会形成两个链表，低位链表还是放在数组原来的位置，高位链表放在j + oldCap的位置。
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```
### remove()

`remove(Object key)`的作用是删除`key`值对应的`entry`，该方法的具体逻辑是在`removeNode()`里实现的。`removeNode()`方法会首先找到`key`值对应的`entry`，然后删除该`entry`（修改链表的相应引用）。
![HashMap_removeEntryForKey](../../PNGFigures/HashMap_removeEntryForKey.png)

```Java
final Node<K,V> removeNode(int hash, Object key, Object value,
                           boolean matchValue, boolean movable) {
    Node<K,V>[] tab; Node<K,V> p; int n, index;
    
    //数组不为空，再通过hash&(tab.length-1)得到对应`bucket`的下标，若该位置没有节点，直接返回nul
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (p = tab[index = (n - 1) & hash]) != null) {
        Node<K,V> node = null, e; K k; V v;
        
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            //数组第一个节点就是要删除的节点，将元素引用赋值给node
            node = p;
        else if ((e = p.next) != null) {//数组第一个节点不是要删除的节点，并且存在hash冲突
            if (p instanceof TreeNode)
                node = ((TreeNode<K,V>)p).getTreeNode(hash, key);//从红黑树中找出要删除的节点
            else {
                //遍历冲突链表，找到要删除的节点
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key ||
                         (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
        }
        
        //若存在需要删除的节点，删除该节点
        if (node != null && (!matchValue || (v = node.value) == value ||
                             (value != null && value.equals(v)))) {   
            if (node instanceof TreeNode)
                //从红黑树中删除该节点
                ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
            else if (node == p)
                //需要删除的entry是头部
                tab[index] = node.next;
            else
                //将父节点的next属性指向删除节点的子节点
                p.next = node.next;
            ++modCount;
            --size;
            afterNodeRemoval(node);
            return node;
        }
    }
    return null;
}
```

## 视图
### KeySet
提供遍历key的迭代器的获取方法
```java
final class KeySet extends AbstractSet<K> {
    public final int size()                 { return size; }
    public final void clear()               { HashMap.this.clear(); }
    public final Iterator<K> iterator()     { return new KeyIterator(); }
    public final boolean contains(Object o) { return containsKey(o); }
    public final boolean remove(Object key) {
        return removeNode(hash(key), key, null, false, true) != null;
    }
    public final Spliterator<K> spliterator() {
        return new KeySpliterator<>(HashMap.this, 0, -1, 0, 0);
    }
}
```

### Values
提供遍历value的迭代器的获取方法
```java
final class Values extends AbstractCollection<V> {
    public final int size()                 { return size; }
    public final void clear()               { HashMap.this.clear(); }
    public final Iterator<V> iterator()     { return new ValueIterator(); }
    public final boolean contains(Object o) { return containsValue(o); }
    public final Spliterator<V> spliterator() {
        return new ValueSpliterator<>(HashMap.this, 0, -1, 0, 0);
    }
}
```

### EntrySet
提供遍历entry的迭代器的获取方法
```java
final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
    public final int size()                 { return size; }
    public final void clear()               { HashMap.this.clear(); }
    public final Iterator<Map.Entry<K,V>> iterator() {
        return new EntryIterator();
    }
    ...
    public final Spliterator<Map.Entry<K,V>> spliterator() {
        return new EntrySpliterator<>(HashMap.this, 0, -1, 0, 0);
    }
}
```

## 迭代器

### HashIterator
```java
abstract class HashIterator {
    Node<K,V> next;        // 下一个需要返回的entry
    Node<K,V> current;     // 当前entry
    int expectedModCount;  // 预期的修改次数，用于快速失败
    int index;             // 当前的数组索引
    
    HashIterator() {
        expectedModCount = modCount;
        Node<K,V>[] t = table;
        current = next = null;
        index = 0;
        if (t != null && size > 0) { // 找到数组第一个entry
            do {} while (index < t.length && (next = t[index++]) == null);
        }
    }
    
    public final boolean hasNext() {
        return next != null;
    }
    
    final Node<K,V> nextNode() {
        Node<K,V>[] t;
        Node<K,V> e = next;//返回next指向的entry
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
        if (e == null)
            throw new NoSuchElementException();
        //将next指向下一个entry，若当前的entry无后继元素，迭代数组，找到下一个entry
        if ((next = (current = e).next) == null && (t = table) != null) {
            do {} while (index < t.length && (next = t[index++]) == null);
        }
        return e;
    }
    
    public final void remove() {
        Node<K,V> p = current;
        if (p == null)
            throw new IllegalStateException();
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
        current = null;
        K key = p.key;
        removeNode(hash(key), key, null, false, false);
        expectedModCount = modCount;
    }
}
```
### KeyIterator
```java
final class KeyIterator extends HashIterator
    implements Iterator<K> {
    public final K next() { return nextNode().key; }
}
```
### ValueIterator
```java
final class ValueIterator extends HashIterator
    implements Iterator<V> {
    public final V next() { return nextNode().value; }
}
```
### EntrySet
```java
final class EntryIterator extends HashIterator
    implements Iterator<Map.Entry<K,V>> {
    public final Map.Entry<K,V> next() { return nextNode(); }
}
```


# HashSet

*HashSet*是对*HashMap*的简单包装，对*HashSet*的函数调用都会转换成合适的*HashMap*方法。
```Java
//HashSet是对HashMap的简单包装
public class HashSet<E>
{
	......
	private transient HashMap<E,Object> map;//HashSet里面有一个HashMap
    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();
    public HashSet() {
        map = new HashMap<>();
    }
    ......
    public boolean add(E e) {//简单的方法转换
        return map.put(e, PRESENT)==null;
    }
    ......
}
```
