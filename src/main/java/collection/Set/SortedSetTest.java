package collection.Set;

import org.junit.Test;

import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetTest {
    @Test
    public void subSetTest(){
        SortedSet<Integer> sortedSet = new TreeSet();
        sortedSet.add(1);
        sortedSet.add(2);
        sortedSet.add(3);
        sortedSet.add(4);
        sortedSet.add(5);

        SortedSet<Integer> subset = sortedSet.subSet(2,4);
        subset.forEach(k-> System.out.println(k));

    }

    @Test
    public void descendingSetTest(){
        NavigableSet navigableSet = new TreeSet();
        navigableSet.add(4);
        navigableSet.add(5);
        navigableSet.add(1);
        navigableSet.add(2);
        navigableSet.add(3);

        System.out.println("ascending:");
        System.out.println("first element:"+ navigableSet.first());
        System.out.println("last element:"+ navigableSet.last());

        NavigableSet descendingSet = navigableSet.descendingSet();
        System.out.println("descending:");
        System.out.println("first element:"+ descendingSet.first());
        System.out.println("last element:"+ descendingSet.last());

        navigableSet.tailSet(2).forEach(k-> System.out.println(k));
    }
}
