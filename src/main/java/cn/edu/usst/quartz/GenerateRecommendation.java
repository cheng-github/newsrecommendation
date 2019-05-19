package cn.edu.usst.quartz;

import cn.edu.usst.algorithm.item.ItemSimilarity;
import cn.edu.usst.algorithm.user.UserSimilarity;
import cn.edu.usst.service.AlgorithmService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;


public class GenerateRecommendation {
    private static final Logger logger = LogManager.getLogger("HelloWorld");

    @Autowired
    AlgorithmService algorithmService;
    @Autowired
    ItemSimilarity itemSimilarity;
    @Autowired
    UserSimilarity userSimilarity;


    public void execute() {
        long start = System.currentTimeMillis();
        List<Map> logResult = algorithmService.getUserClickData();
        /*for (Map map: logResult) {
            String userUUID = map.get("userUUID").toString();
            Long timestamp = ((Timestamp)map.get("timestamp")).getTime(); // 单位为mills不是s
            String newsId = map.get("newsId").toString();
        }*/
        // 将结果给到item和user去使用当做计算
        long startItem = System.currentTimeMillis();
//        ItemSimilarity itemSimilarity = new ItemSimilarity();
        itemSimilarity.calRecommendation(logResult);
        long endItem = System.currentTimeMillis();
//        UserSimilarity userSimilarity = new UserSimilarity();
        userSimilarity.calUserSimilarity(logResult);
        long endUser = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        logger.info("推荐算法调度完成");
        logger.info("物品相似度算法耗时: " + (endItem - startItem)/1000 + "s");
        logger.info("用户相似度算法耗时: " + (endUser - endItem)/1000 + "s");
        logger.info("总耗时:" + (end - start)/1000 + "s");
    }
}
