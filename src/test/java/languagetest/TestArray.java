package languagetest;

import org.junit.Test;

import java.util.HashMap;


public class TestArray {

    @Test
    public void testContains() {
        UserPair.addUserPair(1, 2);
        UserPair.addUserPair(2, 3);
        UserPair.addUserPair(1, 2);
        System.out.println(UserPair.userPairs);
    }

    @Test
    public void testArray() {
        System.out.println(Math.abs(-1.1));
    }

    @Test
    public void testHash() {
        HashMap<String, String> map = new HashMap<>();
        map.put("111", "111");
        setValue(map);
        System.out.println(map);
    }

    private void setValue(HashMap<String, String> map){
        System.out.println(map);
        map.put("222", "222");
    }
}
