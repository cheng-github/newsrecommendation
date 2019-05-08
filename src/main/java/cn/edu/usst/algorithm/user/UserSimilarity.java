package cn.edu.usst.algorithm.user;

import cn.edu.usst.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class UserSimilarity {

    @Autowired
    AlgorithmService algorithmService;

    public void calUserSimilarity(List<Map> clickLog){
        HashMap<String, News> docIdORM = new HashMap(); // 用户新闻映射关系
        Map<String, User> userIdORM = new HashMap(); //

        for (Map map: clickLog){
            String userUUID = map.get("userUUID").toString();
            String newsId = map.get("newsId").toString();
            News obj = null;
            if (!docIdORM.containsKey(newsId)){
                obj = new News(newsId);
                docIdORM.put(newsId, obj);
            } else {
                obj = docIdORM.get(newsId);
            }
            if (!userIdORM.containsKey(userUUID)){
                User us = new User(userUUID);
                us.getReadList().add(obj);
                userIdORM.put(userUUID, us);
            } else {
                userIdORM.get(userUUID).getReadList().add(obj);
            }
        }
        // 根据用户的点击数据去计算minHash矩阵
        int[][] minHash = new int[User.userConstructSerialNumber][60];
        for (int j = 0; j < User.userConstructSerialNumber; j++) {
            Arrays.fill(minHash[j], Integer.MAX_VALUE);
        }
        for (Map.Entry<String, User> entry: userIdORM.entrySet()){
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
//                    第一次先把自己添加到里面，然后如果存在其他人的话说明存在相似的用户
                    similarIdSet.add(i);
                    valueSets.put(lshHash[i][k], similarIdSet);
                }else {
                    // 如果直接添加用户对的话，会导致后面的仅仅可以与前面单个用户建立相似，但这是与我们的预期不符的
                    // 所以应该建立一个数组
                    valueSets.get(lshHash[i][k]).add(i);
                }
            }
            for (ArrayList<Integer> idSets: valueSets.values()) {
                for (int i = 0; i < idSets.size() - 1; i++){
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
            userIdORM.get(User.uuidMatrix.get(userPair.getFirstMatrixId()))
                    .getSimilarList().add(new SimilarUser(User.uuidMatrix.get(userPair.getSecondMatrixId()), similarValue));
            userIdORM.get(User.uuidMatrix.get(userPair.getSecondMatrixId()))
                    .getSimilarList().add(new SimilarUser(User.uuidMatrix.get(userPair.getFirstMatrixId()), similarValue));
        }

        // 根据相似用户生成推荐文章列表，推荐结果的生成应该单独放在一个地方
        List<RecomResult> recList = new ArrayList<>();
        for (Map.Entry<String, User> entry: userIdORM.entrySet()) {
            if (entry.getValue().getSimilarList().size() == 0){
                continue;
            }
            RecomResult recResult = new RecomResult(entry.getKey());
            Collections.sort(entry.getValue().getSimilarList());
            for (SimilarUser similarUser: entry.getValue().getSimilarList()) {
                // 如果这么做的话会导致相似用户的阅读数据都到推荐里面去...会导致数据过高
                // 并且，不同的用户阅读过的一样的东西会导致数据重复的问题
                // 所以我们使用set去做这样的操作，也可以使用list，但需要重写equals以及每次添加前都要做判断
                for(News item: userIdORM.get(similarUser.getUuid()).getReadList()){
                    recResult.getArticleIdList().add(item.getDocument_id());
                }
            }
            // 移除已经存在于当前用户阅读过的内容列表中的文章
            for(News item: entry.getValue().getReadList()){
                recResult.getArticleIdList().remove(item.getDocument_id());
            }
            recList.add(recResult);
        }
//        将结果添加到recom表中去
        for (RecomResult recomResult: recList) {
            Map temp = new HashMap();
            temp.put("userUUID", recomResult.getUserUUID());
            for(String item: recomResult.getArticleIdList()){
                temp.put("newsId", item);
                temp.put("source", "用户相似度算法");
                algorithmService.addRecomItem(temp);
            }
        }
        // 一定要置为0
        User.userConstructSerialNumber = 0;
        News.itemSerialNumber = 0;
        System.out.println("用户相似度计算完成");
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
}
