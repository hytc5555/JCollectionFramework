package collection.Map;

import org.junit.Test;

import java.util.IdentityHashMap;

public class IdentityHashMapTest {

    @Test
    public void identityHashMapTest(){
        IdentityHashMap identityHashMap = new IdentityHashMap(7);
        identityHashMap.put(1,1);
        identityHashMap.put(2,2);
        identityHashMap.put(3,3);
        identityHashMap.put(4,4);
        identityHashMap.put(5,5);
        identityHashMap.remove(2);

    }

}
