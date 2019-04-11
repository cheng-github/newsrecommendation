package languagetest;

import org.junit.Test;
import java.util.ArrayList;


public class TestArray {

    @Test
    public void testContains() {
        UserPair.addUserPair(1, 2);
        UserPair.addUserPair(2, 3);
        UserPair.addUserPair(1, 2);
        System.out.println(UserPair.userPairs);
    }
}
