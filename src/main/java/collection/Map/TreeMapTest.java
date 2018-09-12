package collection.Map;

import org.junit.Test;

import java.util.*;

public class TreeMapTest {
    @Test
    public void treemapTest(){
        TreeMap<String,String> treeMap = new TreeMap<>();

        NavigableSet set1 = treeMap.navigableKeySet();
        //KeyIterator 普通key遍历
        set1.iterator();
        //DescendingKeyIterator 倒叙迭代器,初始为最后一个元素，倒序key遍历
        set1.descendingIterator();

        //转换map为DescendingSubMap
        NavigableSet newSet1 = set1.descendingSet();

        NavigableMap treeMap1 =  treeMap.descendingMap();
        treeMap1.entrySet();

        //获取的keySet中的map为DescendingSubMap
        NavigableSet set2 = treeMap.descendingKeySet();
        //DescendingSubMapKeyIterator 倒叙遍历key
        set2.iterator();
        //SubMapKeyIterator 倒叙遍历key
        set2.descendingIterator();


        Collection collection = treeMap.values();
        //ValueIterator
        collection.iterator();


        Set<Map.Entry<String,String>> entrySet =  treeMap.entrySet();
        //EntryIterator
        entrySet.iterator();


        NavigableMap map = treeMap.descendingMap();
        //DescendingEntrySetView DescendingSubMapEntryIterator
        map.entrySet().iterator();
        //DescendingSubMapKeyIterator
        map.keySet().iterator();
        map.values().iterator();
    }
}
