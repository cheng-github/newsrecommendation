package usersimi;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Java8Test {

    @Test
    public void testStream() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list2.add(3);
        list2.add(4);
        list2.add(5);

        List<Integer> jiaoji = list1.stream().filter( integer -> list2.contains(integer)).collect(Collectors.toList());
        System.out.println(jiaoji.size());
    }
}
