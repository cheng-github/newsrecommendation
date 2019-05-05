package cn.edu.usst.algorithm.item;

import java.sql.Timestamp;
import java.util.*;

public class ItemSimilarity {


    public static void calRecommendation(List<Map> clickLog){
        Map<String, ArrayList<News>> userClick = new HashMap<>();

        for (Map map: clickLog) {
            String userUUID = map.get("userUUID").toString();
            String newsId = map.get("newsId").toString();
            Long timestamp = ((Timestamp)map.get("timestamp")).getTime(); // 单位为mills不是s
            if (!userClick.containsKey(userUUID)){
                News item = new News(newsId, timestamp);
                ArrayList<News> readList = new ArrayList<>();
                readList.add(item);
                userClick.put(userUUID, readList);
            }else {
                News item = new News(newsId, timestamp);
                userClick.get(userUUID).add(item);
            }
        }

        // 针对读取出来的数据，我们对物品建立一个相似度列表
        HashMap<String, HashMap<String, Integer>> itemSimi = new HashMap<>();
        for (ArrayList<News> articleList: userClick.values()){
            Collections.sort(articleList);
            for(int i = 0; i < articleList.size() - 1; i++){
                for (int j = i + 1; j < articleList.size(); j++) {
                    News first = articleList.get(i);
                    News second = articleList.get(j);
                    if (Math.abs(first.getTimeStamp() - second.getTimeStamp()) > 7200000){
                        // 直接跳出循环，进行下一个比较
                        break;
                    }else {
                        // 那么对于不同的用户来说，可能存在一个在前一个在后，所以先后顺序是不确定的
                        // 在这种情况下，不如我们建立一次将这个建立好，因为挨个去找其实是很耗时的
                        putSimiInfo(first, second, itemSimi);
                        putSimiInfo(second, first, itemSimi);
                    }
                }
            }
        }

        // 根据这个相似度列表以及用户的阅读历史进行推荐，而且应该根据所有点击的权重去获取
        List<RecResult> recOutput = new ArrayList<>();
        for (Map.Entry<String, ArrayList<News>> entry: userClick.entrySet()){
            RecResult recItem = new RecResult(entry.getKey());
            ArrayList<CandidateNews> cancList = new ArrayList<>();
            ArrayList<News> readList = entry.getValue();
            for (News item: readList){
                // 注意，对于有些item,不存在对应的相似新闻，所以我们在遍历获取之前需要确保里面有值
                // 否则会抛出空指针异常
                if (itemSimi.get(item.getNewsId()) == null){
                    continue;
                }
                for (Map.Entry<String, Integer> entry1: itemSimi.get(item.getNewsId()).entrySet()){
                    // 直接去掉已经阅读过的文章
                    if (readList.contains(new News(entry1.getKey(), 0))){
                        continue;
                    }
                    // 太小的值直接去掉，避免推荐结果过多，以及需要去掉已经阅读过的文章
                    // Mark --- 可能不需要去掉在没有数据的情况下
                    if (entry1.getValue() < 100){
                        continue;
                    }
                    // 如果既不是已经阅读过，也不是权重过少，那么将其添加的候选列表
                    CandidateNews candidateNews = new CandidateNews(entry1.getKey(), entry1.getValue());
                    cancList.add(candidateNews);
                }
            }
            recItem.setRecList(cancList);
            recOutput.add(recItem);
        }
        // 将所有推荐的结果按照权重进行排序
        for(RecResult recResult: recOutput){
            Collections.sort(recResult.getRecList());
        }
        // 输出限制结果即可
        System.out.println("输出推荐结果完毕");


    }


    /**
     * 将这两个新闻添加到对应的map里去
     * @param first
     * @param second
     */
    private static void putSimiInfo(News first, News second, HashMap<String, HashMap<String, Integer>> itemSimi){
        if (!itemSimi.containsKey(first.getNewsId())){
            HashMap<String, Integer> simiSets = new HashMap<>();
            simiSets.put(second.getNewsId(), 1);
            itemSimi.put(first.getNewsId(), simiSets);
        } else {
            if (!itemSimi.get(first.getNewsId()).containsKey(second.getNewsId())){
                itemSimi.get(first.getNewsId()).put(second.getNewsId(), 1);
            } else {
                Integer temp = itemSimi.get(first.getNewsId()).get(second.getNewsId()) + 1;
                itemSimi.get(first.getNewsId()).put(second.getNewsId(), temp);
            }
        }
    }

}
