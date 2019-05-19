package cn.edu.usst.algorithm.item;

import cn.edu.usst.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Component
public class ItemSimilarity {

    @Autowired
    AlgorithmService algorithmService;

    public void calRecommendation(List<Map> clickLog){
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
                    // 设定为两个小时内的点击
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
                if (itemSimi.get(item.getNewsId()) == null) {
                    continue;
                }
                for (Map.Entry<String, Integer> entry1: itemSimi.get(item.getNewsId()).entrySet()){
                    // 直接去掉已经阅读过的文章
                    if (readList.contains(new News(entry1.getKey(), 0))){
                        continue;
                    }
                    // 太小的值直接去掉，避免推荐结果过多，以及需要去掉已经阅读过的文章，所以设为了100
                    // Mark --- 可能不需要去掉在没有数据的情况下，所以我们设为10好了，因为我们只是测试系统
                    if (entry1.getValue() < 10){
                        continue;
                    }
                    // 如果既不是已经阅读过，也不是权重过少，那么将其添加的候选列表
                    // 去除可能存在的重复
                    CandidateNews candidateNews = new CandidateNews(entry1.getKey(), entry1.getValue());
                    if (!cancList.contains(candidateNews))
                        cancList.add(candidateNews);
                }
            }
            recItem.setRecList(cancList);
            recOutput.add(recItem);
        }
        // 将所有推荐的结果按照权重进行排序并加入到推荐列表里去
        for(RecResult recResult: recOutput){
            Collections.sort(recResult.getRecList());
            Map temp = new HashMap();
            temp.put("userUUID", recResult.getUserId());
            ArrayList<CandidateNews> recList = recResult.getRecList();
            for (CandidateNews addItem: recList) {
                temp.put("newsId", addItem.getNewsId());
                temp.put("source", "物品相似度算法");
                algorithmService.addRecomItem(temp);
            }
        }
        // 输出限制结果并将推荐结果写入数据库，我们目前可能不需要限制，因为结果可能太少，再去做结果限制就会
        // 看不到效果
        System.out.println("物品相似度输出推荐结果完毕");


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
