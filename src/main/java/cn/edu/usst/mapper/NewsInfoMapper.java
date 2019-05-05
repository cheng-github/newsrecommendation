package cn.edu.usst.mapper;

import java.util.List;
import java.util.Map;

public interface NewsInfoMapper {

    List getHomePageNewsListData(Map map);

    Integer getTotalHomePageNumber();

    Map getNewsDetail(String newsid);

}
