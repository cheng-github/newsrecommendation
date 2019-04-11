package languagetest;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestHashMap {

    @Test
    public void testHashMap() {
        Map<Integer, Integer> valueSets = new HashMap<>();
        valueSets.put(111, 2);
        valueSets.put(222, 2);
        System.out.println(valueSets.containsKey(222));
        Integer a = new Integer(222);
        Integer b = new Integer(222);
        // 因为Integer对象重写了hashCode方法，但是 == 是对比的地址
        System.out.println(a.hashCode() == b.hashCode());
        System.out.println(a == b);
        System.out.println(a.equals(b));
    }

    @Test
    public void testCal() {
        int a = 11;
        int b = 22;
        System.out.println((float)b);
        float c = a / (float)b;
        System.out.println(c);
    }
}
