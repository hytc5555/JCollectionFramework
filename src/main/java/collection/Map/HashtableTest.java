package collection.Map;

import org.junit.Test;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class HashtableTest {
    @Test
    public void hashtableTest(){
        Map map = new HashMap();
        map.put(1,1);
        map.put(2,2);
        map.put(3,3);
        map.put(4,1);
        map.put(5,2);
        map.put(6,3);

        Hashtable hashtable = new Hashtable(map);

        hashtable.put(null,1);
    }
}
