package collection.Map;

import org.junit.Test;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;

public class EnumMapTest {

    @Test
    public void enumMapTest(){
        EnumMap<EnumTest,String> enumMap = new EnumMap<>(EnumTest.class);
        enumMap.put(EnumTest.Enum2,"2");
        enumMap.put(EnumTest.Enum3,null);

        Iterator it =  enumMap.values().iterator();
        while (it.hasNext())
            System.out.println(it.next());

        Iterator iterator = enumMap.entrySet().iterator();

/*        HashMap hashMap = new HashMap();
        hashMap.put(1,1);

        EnumMap enumMap1 = new EnumMap(hashMap);
        enumMap.forEach((k,v)-> System.out.println(k+"="+v));*/
    }


}
