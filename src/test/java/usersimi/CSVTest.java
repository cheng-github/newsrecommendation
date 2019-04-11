package usersimi;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.FileReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

public class CSVTest {

    @Test
    public void csvDemo() throws Exception{
        int line_count = 0;
//        训练集文件直接读取肯定会爆掉
//        读取文件有两种方式，一种是一次性全部将文件的内容放到内存里，一种是一次拿一点，显然是后者要优于前者
//        尤其是文件内容过大的时候，这里也是这样，这个java库里的parse也是使用的BufferedReader而非
        Reader in = new FileReader("F:\\毕业设计_Stuff\\outbrain-click-prediction\\分割文件_1000000_views" +
                "\\page_views_sample-000.csv");
//       Reader in = new FileReader("F:\\毕业设计_Stuff\\outbrain-click-prediction\\clicks_train.csv");
       Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("uuid","document_id","timestamp","platform","geo_location",
               "traffic_source").parse(in);

       for (CSVRecord record : records) {
           String lastName = record.get("uuid");
           String firstName = record.get("document_id");
           System.out.println(firstName + "," + lastName);
           if (line_count++ > 100)
               break;
       }
    }

    /**
     * 十五个文件还是太久了，我们在编写调试程序的时候，可以先把文件数目降低，比如先到1000w即可
     * @throws Exception
     */
    @Test
    public void testIfHasSameUserUUID() throws Exception{
        int fileNumCount = 0;
        String prefix = "E:\\毕业设计分割的大文件\\";
        String[] fileSubPaths = new String[]{"page_views-000.csv", "page_views-001.csv",
                "page_views-002.csv","page_views-003.csv", "page_views-004.csv",
              /*  "page_views-005.csv","page_views-006.csv", "page_views-007.csv",
                "page_views-008.csv","page_views-009.csv", "page_views-010.csv",
                "page_views-011.csv","page_views-012.csv", "page_views-013.csv",
                "page_views-014.csv"*/
        };
        Set user = new HashSet<String>();
        Set document = new HashSet<String>();
        for (int i = 0; i < fileSubPaths.length; i++) {
            Reader in = new FileReader(prefix + fileSubPaths[i]);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("uuid","document_id","timestamp","platform","geo_location",
                    "traffic_source").parse(in);
            for(CSVRecord csvRecord: records){
                user.add(csvRecord.get("uuid"));
                document.add(csvRecord.get("document_id"));
            }
            System.out.println("当前第" + (++fileNumCount) + "个文件");
        }
        user.remove("uuid");
        document.remove("document_id");
        // 理论上一共3000w条数据，一个uuid字符串
        System.out.println("uuid数目:" + user.size());
        System.out.println("document数目:" + document.size());
    }

    @Test
    public void testHashSet(){
        Set sets = new HashSet<Integer>();
        sets.add(1);
        sets.add(2);
        sets.add(1);
        System.out.println(sets.size());
    }
}
