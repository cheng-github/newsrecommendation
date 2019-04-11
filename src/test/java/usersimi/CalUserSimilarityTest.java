package usersimi;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class CalUserSimilarityTest {

    @Test
    public void calSimilarity() {

    }

    @Test
    public void obtainUserSimilarityMatrix()throws Exception{
        int fileNumCount = 0;
//        String prefix = "E:\\毕业设计分割的大文件\\";
//        String[] fileSubPaths = new String[]{
////                "page_views-000.csv",
//                "page_views-001.csv",
////                "page_views-002.csv","page_views-003.csv", "page_views-004.csv",
//                /*  "page_views-005.csv","page_views-006.csv", "page_views-007.csv",
//                  "page_views-008.csv","page_views-009.csv", "page_views-010.csv",
//                  "page_views-011.csv","page_views-012.csv", "page_views-013.csv",
//                  "page_views-014.csv"*/
//        };
        String prefix = "F:\\毕业设计_Stuff\\movielens-ml-latest-small\\ml-latest-small\\";
        String[] fileSubPaths = new String[]{
                "ratings.csv"
        };
        for (int i = 0; i < fileSubPaths.length; i++) {
            Reader in = new FileReader(prefix + fileSubPaths[i]);
//            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("uuid","document_id","timestamp","platform","geo_location",
//                    "traffic_source").parse(in);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader("userId","movieId","rating","timestamp").parse(in);
            int lineNumber = 0;
            for(CSVRecord csvRecord: records){
                // 不添加第一行的数据
                if (lineNumber == 0){
                    lineNumber = 1;
                    continue;
                }
//                usersimi.News newItem = usersimi.News.addNewsInfo(csvRecord.get("document_id"));
//                usersimi.User.addUserInfo(csvRecord.get("uuid"), newItem);
                News newItem = News.addNewsInfo(csvRecord.get("movieId"));
                User.addUserInfo(csvRecord.get("userId"), newItem);
            }
            System.out.println("当前第" + (++fileNumCount) + "个文件");
        }
        // 不可以直接创建这样的矩阵，因为内存不允许，所以我们需要构建hash函数去缩小这个
        // 矩阵的大小，问题是如何构建五十个hash函数
        int[][] minHash = new int[User.userConstructSerialNumber][60];
        for (int j = 0; j < User.userConstructSerialNumber; j++) {
            Arrays.fill(minHash[j], Integer.MAX_VALUE);
        }
        for (Map.Entry<String, User> entry: User.userIdORM.entrySet()){
            User user = entry.getValue();
            for (int l = 0; l < 60; l++) {
                // 对于每个用户，其中的60个hash函数我们仅仅需要对每个hash函数去计算它们存在有新闻的行号，
                // 这样减少了计算量
                for (News item :user.getReadList()){
                    if (minHash[user.getMatrixNumber()][l] > handleHashFunction(l, item.getMatrixNumber()))
                        minHash[user.getMatrixNumber()][l] =  handleHashFunction(l, item.getMatrixNumber());
                }
            }
        }
        // 使用LSH进行对矩阵的压缩，即将六十行的数组分为二十个桶，每个桶为四行。计算出相似度可能会很高的两个用户,
        // 忽略掉相似度较小的用户，达到减少计算量的目的。
        int[][] lshHash = new int[User.userConstructSerialNumber][15];
        for (int i = 0; i < User.userConstructSerialNumber; i++) {
            for (int j = 0; j < 15; j++) {
                int[] intArgs =
                        new int[]{minHash[i][j * 4],minHash[i][j * 4 + 1],minHash[i][j * 4 + 2],minHash[i][j * 4 + 3]};
                lshHash[i][j] = calBucketValue(intArgs);
            }
        }
        // 找到相似度较高的用户,仅仅计算它们的相似度，然后得到一个相似度列表
        for (int k = 0; k < 15; k++) {
            Map<Integer, ArrayList<Integer>> valueSets = new HashMap<>();
            for (int i = 0; i < User.userConstructSerialNumber; i++) {
                if (!valueSets.containsKey(lshHash[i][k])){
                    ArrayList<Integer> similarIdSet = new ArrayList<>();
                    similarIdSet.add(i);
                    valueSets.put(lshHash[i][k], similarIdSet);
                }else {
                    // 如果直接添加用户对的话，会导致后面的仅仅可以与前面单个用户建立相似，但这是与我们的预期不符的
                    // 所以应该建立一个数组
                    valueSets.get(lshHash[i][k]).add(i);
                }
            }
            for (ArrayList<Integer> idSets: valueSets.values()) {
                for (int i = 0;i < idSets.size() - 1; i++){
                    for (int j = i + 1; j < idSets.size(); j++) {
                        UserPair.addUserPair(idSets.get(i), idSets.get(j));
                    }
                }
            }
        }
        // 计算相似性，将相似的结果都存入到用户对象里去
        for (UserPair userPair: UserPair.userPairs) {
            int sameItemNum = 0;
            float similarValue;
            for (int i = 0; i < 60; i++) {
                if (minHash[userPair.getFirstMatrixId()][i] == minHash[userPair.getSecondMatrixId()][i]){
                    sameItemNum++;
                }
            }
            // 去掉一些碰巧的hash到同样的lsh，以及一些相似度过低的用户，这里选择的是将0.1一下的用户去除掉
            if (sameItemNum < 11)
                continue;
            similarValue = sameItemNum / (float)(120 - sameItemNum);
            // 除去相等的hash值那么剩下的行都是不相等的
            User.userIdORM.get(User.uuidMatrix.get(userPair.getFirstMatrixId()))
                    .getSimilarList().add(new SimilarUser(User.uuidMatrix.get(userPair.getSecondMatrixId()), similarValue));
            User.userIdORM.get(User.uuidMatrix.get(userPair.getSecondMatrixId()))
                    .getSimilarList().add(new SimilarUser(User.uuidMatrix.get(userPair.getFirstMatrixId()), similarValue));
        }
        // 根据相似用户生成推荐文章列表，不放到User对象中去了，先丢到一个Result对象中去得了
        List<RecomResult> recList = new ArrayList<>();
        for (Map.Entry<String, User> entry: User.userIdORM.entrySet()) {
            if (entry.getValue().getSimilarList().size() == 0){
                continue;
            }
            RecomResult recResult = new RecomResult(entry.getKey());
            Collections.sort(entry.getValue().getSimilarList());
            for (SimilarUser similarUser: entry.getValue().getSimilarList()) {
                recResult.getArticleIdList().addAll(User.userIdORM.get(similarUser.getUuid()).getReadList());
            }
            // 移除已经存在于当前用户阅读过的内容列表中的文章
            recResult.getArticleIdList().removeAll(entry.getValue().getReadList());
            recList.add(recResult);
        }
        System.out.println("已经输出所有推荐结果");
    }

    /**
     * 返回minHash签名矩阵中的值
     * @param hashFuncOrder 表示第多少个hash函数
     * @param rowNumber 需要计算的行号值
     * @return
     */
    private int handleHashFunction(int hashFuncOrder, int rowNumber){
        switch (hashFuncOrder){
            case 0:
                return (rowNumber * 9000 + 41228) % News.itemSerialNumber;
            case 1:
                return (rowNumber * 6973 + 61778) % News.itemSerialNumber;
            case 2:
                return (rowNumber * 1411 + 52426) % News.itemSerialNumber;
            case 3:
                return (rowNumber * 6321 + 13674) % News.itemSerialNumber;
            case 4:
                return (rowNumber * 1121 + 95183) % News.itemSerialNumber;
            case 5:
                return (rowNumber * 7215 + 66882) % News.itemSerialNumber;
            case 6:
                return (rowNumber * 9719 + 47878) % News.itemSerialNumber;
            case 7:
                return (rowNumber * 4311 + 99812) % News.itemSerialNumber;
            case 8:
                return (rowNumber * 5484 + 49159) % News.itemSerialNumber;
            case 9:
                return (rowNumber * 4822 + 55012) % News.itemSerialNumber;
            case 10:
                return (rowNumber * 1113 + 54452) % News.itemSerialNumber;
            case 11:
                return (rowNumber * 2319 + 46174) % News.itemSerialNumber;
            case 12:
                return (rowNumber * 6576 + 27284) % News.itemSerialNumber;
            case 13:
                return (rowNumber * 7324 + 52971) % News.itemSerialNumber;
            case 14:
                return (rowNumber * 1715 + 48151) % News.itemSerialNumber;
            case 15:
                return (rowNumber * 4097 + 18129) % News.itemSerialNumber;
            case 16:
                return (rowNumber * 1999 + 11947) % News.itemSerialNumber;
            case 17:
                return (rowNumber * 9726 + 76777) % News.itemSerialNumber;
            case 18:
                return (rowNumber * 3526 + 66754) % News.itemSerialNumber;
            case 19:
                return (rowNumber * 3944 + 15757) % News.itemSerialNumber;
            case 20:
                return (rowNumber * 1595 + 32221) % News.itemSerialNumber;
            case 21:
                return (rowNumber * 6590 + 54365) % News.itemSerialNumber;
            case 22:
                return (rowNumber * 7651 + 74534) % News.itemSerialNumber;
            case 23:
                return (rowNumber * 8121 + 12737) % News.itemSerialNumber;
            case 24:
                return (rowNumber * 44391 + 64647) % News.itemSerialNumber;
            case 25:
                return (rowNumber * 6633 + 84211) % News.itemSerialNumber;
            case 26:
                return (rowNumber * 8113 + 56272) % News.itemSerialNumber;
            case 27:
                return (rowNumber * 51111 + 73232) % News.itemSerialNumber;
            case 28:
                return (rowNumber * 94343 + 12537) % News.itemSerialNumber;
            case 29:
                return (rowNumber * 12172 + 67892) % News.itemSerialNumber;
            case 30:
                return (rowNumber * 22125 + 97341) % News.itemSerialNumber;
            case 31:
                return (rowNumber * 31110 + 52112) % News.itemSerialNumber;
            case 32:
                return (rowNumber * 52215 + 68051) % News.itemSerialNumber;
            case 33:
                return (rowNumber * 76239 + 12554) % News.itemSerialNumber;
            case 34:
                return (rowNumber * 69267 + 58104) % News.itemSerialNumber;
            case 35:
                return (rowNumber * 62253 + 46812) % News.itemSerialNumber;
            case 36:
                return (rowNumber * 19331 + 23561) % News.itemSerialNumber;
            case 37:
                return (rowNumber * 18431 + 98512) % News.itemSerialNumber;
            case 38:
                return (rowNumber * 1277 + 87123) % News.itemSerialNumber;
            case 39:
                return (rowNumber * 7174 + 81228) % News.itemSerialNumber;
            case 40:
                return (rowNumber * 9837 + 83165) % News.itemSerialNumber;
            case 41:
                return (rowNumber * 9515 + 62112) % News.itemSerialNumber;
            case 42:
                return (rowNumber * 2198 + 64215) % News.itemSerialNumber;
            case 43:
                return (rowNumber * 4277 + 21212) % News.itemSerialNumber;
            case 44:
                return (rowNumber * 5341 + 64215) % News.itemSerialNumber;
            case 45:
                return (rowNumber * 3476 + 98541) % News.itemSerialNumber;
            case 46:
                return (rowNumber * 18583 + 64230) % News.itemSerialNumber;
            case 47:
                return (rowNumber * 23671 + 33320) % News.itemSerialNumber;
            case 48:
                return (rowNumber * 34276 + 32141) % News.itemSerialNumber;
            case 49:
                return (rowNumber * 9111 + 83230) % News.itemSerialNumber;
            case 50:
                return (rowNumber * 5937 + 23565) % News.itemSerialNumber;
            case 51:
                return (rowNumber * 1515 + 62212) % News.itemSerialNumber;
            case 52:
                return (rowNumber * 2218 + 62925) % News.itemSerialNumber;
            case 53:
                return (rowNumber * 33274 + 11872) % News.itemSerialNumber;
            case 54:
                return (rowNumber * 8141 + 28715) % News.itemSerialNumber;
            case 55:
                return (rowNumber * 1276 + 21241) % News.itemSerialNumber;
            case 56:
                return (rowNumber * 7621 + 64990) % News.itemSerialNumber;
            case 57:
                return (rowNumber * 45122 + 38320) % News.itemSerialNumber;
            case 58:
                return (rowNumber * 76121 + 32841) % News.itemSerialNumber;
            case 59:
                return (rowNumber * 9201 + 23730) % News.itemSerialNumber;

        }
        return Integer.MAX_VALUE;
    }

    @Test
    public void testBucketValue() {
        int[] first = new int[]{1466, 332, 285, 366};
        int[] second = new int[]{14, 2, 61, 0};
        News.itemSerialNumber = 9066;
        System.out.println(calBucketValue(first));
        System.out.println(calBucketValue(second));
    }

    /**
     * 将传递过来的四个数字进行hash，然后将它们的hash结果返回
     * 我们可以尽量让这个结果分散，因为这样的话就可以保证它们是相等的
     * @param args
     * @return
     */
    private int calBucketValue(int[] args){
        int result = 0;
        for (int j = 0; j < args.length; j++) {
            switch (j){
                case 0:
                    result += ((args[j] * 23511 + 19211) % News.itemSerialNumber);
                    break;
                case 1:
                    result += ((args[j] * 87511 + 38711) % News.itemSerialNumber);
                    break;
                case 2:
                    result += ((args[j] * 54321 + 49742) % News.itemSerialNumber);
                    break;
                case 3:
                    result += ((args[j] * 46791 + 86426) % News.itemSerialNumber);
                    break;
            }
        }
        return result;
    }

    /**
     * 未初始化的数组的值全部都是0
     */
    @Test
    public void testArray(){
        int[][] arr = new int[3][3];
        System.out.println(arr[0][0]);
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Math.pow(2, 31)-1);
        System.out.println(94343 * 60000);
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

    @Test
    public void testAddall() {
        List<Integer> listSum = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        listSum.add(1);
        listSum.add(2);
        System.out.println(listSum);
        list1.add(3);
        list2.add(4);
        list1.add(5);
        list2.add(6);
        listSum.addAll(list1);
        System.out.println(listSum);
        listSum.addAll(list2);
        System.out.println(listSum);
        listSum.removeAll(list1);
        System.out.println(listSum);
    }

    @Test
    public void testHashMapMultipeValue() {
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 2);
        map1.put(1, 3);
        map1.put(2, 3);
        System.out.println(map1);
    }


}
