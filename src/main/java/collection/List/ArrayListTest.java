package collection.List;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListTest {
    public static void main(String[] args) {
        // 使用Lambda表达式实现
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.replaceAll(str -> {
            if(str.length()>3)
                return str.toUpperCase();
            return str;
        });

        list.forEach(k-> System.out.println(k));
    }
}

