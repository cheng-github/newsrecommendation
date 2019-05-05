package cn.edu.usst.quartz;

import cn.edu.usst.service.AlgorithmService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public class GenerateRecommendation {
    private static final Logger logger = LogManager.getLogger("HelloWorld");

    @Autowired
    AlgorithmService algorithmService;

    public void execute() {
        List<Map> logResult = algorithmService.getUserClickData();
        /*for (Map map: logResult) {
            String userUUID = map.get("userUUID").toString();
            Long timestamp = ((Timestamp)map.get("timestamp")).getTime(); // 单位为mills不是s
            String newsId = map.get("newsId").toString();
        }*/
        // 将结果给到item和user去使用当做计算

        logger.info("Hello, World!");
    }
}
