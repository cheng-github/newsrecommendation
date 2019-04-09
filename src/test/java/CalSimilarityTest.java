import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
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
        // 不可以直接创建这样的矩阵，因为内存不允许，所以我们需要构建hash函数去缩小这个
        // 矩阵的大小，问题是如何构建五十个hash函数
        int[][] minHash = new int[User.userConstructSerialNumber][50];
        for (int j = 0; j < User.userConstructSerialNumber; j++) {
            Arrays.fill(minHash[j], Integer.MAX_VALUE);
        }
        for (Map.Entry<String, User> entry: User.userIdORM.entrySet()){
            User user = entry.getValue();
            for (int l = 0; l < 50; l++) {
                // 对于每个用户，其中的五十个hash函数我们仅仅需要对每个hash函数去计算它们存在有新闻的行号，
                // 这样减少了计算量
                for (News item :user.getReadList()){
                    if (minHash[user.getMatrixNumber()][l] > handleHashFunction(l, item.getMatrixNumber()))
                        minHash[user.getMatrixNumber()][l] =  handleHashFunction(l, item.getMatrixNumber());
                }
            }
        }

    }

    /**
     * 返回minHash签名矩阵中的值
     * @param hashFuncOrder 表示第多少个hash函数
     * @param rowNumber 需要计算的行号值
     * @return
     */
    private int handleHashFunction(int hashFuncOrder,int rowNumber){
        switch (hashFuncOrder){
            case 0:
                return (rowNumber * 9 + 4) % News.itemSerialNumber;
            case 1:
                return (rowNumber * 6 + 67) % News.itemSerialNumber;
            case 2:
                return (rowNumber * 14 + 54) % News.itemSerialNumber;
            case 3:
                return (rowNumber * 32 + 167) % News.itemSerialNumber;
        }
        return Integer.MAX_VALUE;
    }

    /**
     * 未初始化的数组的值全部都是0
     */
    @Test
    public void testArray(){
        int[][] arr = new int[3][3];
        System.out.println(arr[0][0]);
    }

    /**
     * return之后不会再执行
     */
    @Test
    public void testSwitch(){
        int chooseValue = 1;
        switch (chooseValue){
            case 0:
                System.out.println("0");
                return ;
            case 1:
                System.out.println("1");
                return ;
            case 2:
                System.out.println("2");
                return ;
        }
        System.out.println("return");
        return ;
    }
}
