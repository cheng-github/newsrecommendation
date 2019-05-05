package cn.edu.usst.service.impl;

import cn.edu.usst.mapper.NewsInfoMapper;
import cn.edu.usst.model.NewsListRequest;
import cn.edu.usst.service.NewsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsInfoImp implements NewsInfoService {

    @Autowired
    NewsInfoMapper newsInfoMapper;

    /**
     * 获取到首页新闻列表
     * @param newsListRequest
     * @return
     */
    @Override
    public List<Map> getHomePageNewsList(NewsListRequest newsListRequest) {
        Map param = new HashMap();
        Integer offset = newsListRequest.getRequestPageNumber() * 15;
        param.put("offset", offset);
        return newsInfoMapper.getHomePageNewsListData(param);
    }

    @Override
    public Integer getTotalHomePageNumber() {
        return newsInfoMapper.getTotalHomePageNumber();
    }


    @Override
    public Map getNewsDetail(String newsid) {
        Map temp = newsInfoMapper.getNewsDetail(newsid);
        Map result = new HashMap();

        result.put("title", temp.get("title"));
        String[] contents = ((String)temp.get("article_content"))
                .replaceAll("　", " ").split("  ");
        List<String> sections = new ArrayList<>();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i].length() < 4){
                continue;
            }else {
                sections.add("　　" + contents[i]); // 由于英文的空格会被p直接trim掉，所以我们还是使用大空格
            }
        }
        result.put("sections", sections);
        return result;
    }
}
