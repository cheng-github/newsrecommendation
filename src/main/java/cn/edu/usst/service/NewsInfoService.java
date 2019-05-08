package cn.edu.usst.service;

import cn.edu.usst.model.NewsListRequest;

import java.util.List;
import java.util.Map;

public interface NewsInfoService {

    List<Map> getHomePageNewsList(NewsListRequest newsListRequest);

    Integer getTotalHomePageNumber();

    Map getNewsDetail(String newsid);

    List<Map> getNewsListByCategory(NewsListRequest newsListRequest);

    Integer getTotalNumByCategory(NewsListRequest newsListRequest);

    List<Map> getRecommendationNewsList(String userUUID);

    Integer getTotalNumRecom(String userUUID);

    List<Map> getHotDataForRemcom(Map map);
}
