package cn.edu.usst.mapper;

import java.util.List;
import java.util.Map;

public interface RecommendationMapper {

    List<Map> getUserClickData();

    Integer checkRecomItem(Map map);

    Integer addRecomItem(Map map);
}
