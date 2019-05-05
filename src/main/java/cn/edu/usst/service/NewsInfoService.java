package cn.edu.usst.service;

import cn.edu.usst.model.NewsListRequest;

import java.util.List;
import java.util.Map;

public interface NewsInfoService {

    List<Map> getHomePageNewsList(NewsListRequest newsListRequest);

    Integer getTotalHomePageNumber();

    Map getNewsDetail(String newsid);
}
