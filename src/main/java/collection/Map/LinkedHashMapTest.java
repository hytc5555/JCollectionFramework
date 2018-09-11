package collection.Map;

import org.junit.Test;

import java.util.LinkedHashMap;

public class LinkedHashMapTest {
    @Test
    public void accessOrderTest(){
        LinkedHashMap linkedHashMap = new LinkedHashMap(10,0.75f,true);
        linkedHashMap.put(1,1);
        linkedHashMap.put(2,2);
        linkedHashMap.put(3,3);
        linkedHashMap.put(4,4);
        linkedHashMap.put(5,5);

        linkedHashMap.get(5);
    }
}
