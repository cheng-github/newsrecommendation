import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.FileReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CalSimilarityTest {

    @Test
    public void calSimilarity() {

    }

    @Test
    public void obtainItemUserMatrix()throws Exception{
        int fileNumCount = 0;
        String prefix = "E:\\毕业设计分割的大文件\\";
        String[] fileSubPaths = new String[]{"page_views-000.csv", "page_views-001.csv",
                "page_views-002.csv","page_views-003.csv", "page_views-004.csv",
                /*  "page_views-005.csv","page_views-006.csv", "page_views-007.csv",
                  "page_views-008.csv","page_views-009.csv", "page_views-010.csv",
                  "page_views-011.csv","page_views-012.csv", "page_views-013.csv",
                  "page_views-014.csv"*/
        };
        for (int i = 0; i < fileSubPaths.length; i++) {
            Reader in = new FileReader(prefix + fileSubPaths[i]);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("uuid","document_id","timestamp","platform","geo_location",
                    "traffic_source").parse(in);
            int lineNumber = 0;
            for(CSVRecord csvRecord: records){
                // 不添加第一行的数据
                if (lineNumber == 0){
                    lineNumber = 1;
                    continue;
                }
                News newItem = News.addNewsInfo(csvRecord.get("document_id"));
                User.addUserInfo(csvRecord.get("uuid"), newItem);
            }
            System.out.println("当前第" + (++fileNumCount) + "个文件");
        }
        // 不可以直接创建这样的矩阵，因为内存不允许
        int[][] usernewsMatrix = new int[User.userConstructSerialNumber][News.itemSerialNumber];
        for (Map.Entry<String, User> entry: User.userIdORM.entrySet()){
            User user = entry.getValue();
            for (News item :user.getReadList())
                usernewsMatrix[user.getMatrixNumber()][item.getMatrixNumber()] = 1;
        }
        System.out.println("用户新闻矩阵初步构建完成");
    }

    /**
     * 未初始化的数组的值全部都是0
     */
    @Test
    public void testArray(){
        int[][] arr = new int[3][3];
        System.out.println(arr[0][0]);
    }
}
