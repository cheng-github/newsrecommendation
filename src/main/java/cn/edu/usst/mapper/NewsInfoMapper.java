package cn.edu.usst.mapper;

import java.util.List;
import java.util.Map;

public interface NewsInfoMapper {

    List getHomePageNewsListData(Map map);

    Integer getTotalHomePageNumber();

    Map getNewsDetail(String newsid);

    List<Map> getNewsListByCategory(Map map);

    Integer getTotalNumByCategory(Map map);

    List<Map> getRecommendationNewsList(String userUUID);

    Integer getTotalNumRecom(String userUUID);

    List<Map> getHotDataForRemcom(Map map);
}
