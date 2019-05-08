package cn.edu.usst.controller;

import cn.edu.usst.model.NewsListRequest;
import cn.edu.usst.service.ClickActionService;
import cn.edu.usst.service.NewsInfoService;
import cn.edu.usst.service.impl.NewsInfoImp;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("news")
public class NewsController {

    @Autowired
    NewsInfoService newsInfoImp;

    @Autowired
    ClickActionService clickActionService;

    @RequestMapping(value = "")
    public String getNewsListView(){
        return "newslist";
    }

    @RequestMapping(value = "/newscontent")
    public String getNewsContentView(String newsid){
        // 将传递过来的newsid写入clicklog表中
        String useruuid = SecurityUtils.getSubject().getSession().getAttribute("uuid").toString();
        Map param = new HashMap();
        param.put("useruuid", useruuid);
        param.put("newsid", newsid);
        clickActionService.handleClickAction(param);
        return "newscontent";
    }

    @RequestMapping(value = "/newsdataraw", method = RequestMethod.GET)
    @ResponseBody
    public Map getNewsContentData(String newsid){
        return newsInfoImp.getNewsDetail(newsid);
    }

    @RequestMapping(value = "/newslist", method = RequestMethod.GET)
    @ResponseBody
    public Map getNewsListData(NewsListRequest newsListRequest){
        List<Map> newsListData = null;
        Integer totalNum = 0;
        Map result = new HashMap();

        switch (newsListRequest.getRequestType()){
            case "homepage":
                newsListData = newsInfoImp.getHomePageNewsList(newsListRequest);
                totalNum = newsInfoImp.getTotalHomePageNumber();
                break;
            case "recommendation":
                String useruuid = SecurityUtils.getSubject().getSession().getAttribute("uuid").toString();
                newsListData = newsInfoImp.getRecommendationNewsList(useruuid);
                totalNum = newsInfoImp.getTotalNumRecom(useruuid);
                // 如果使用算法推荐的数目太少，我们应该根据阅读数推荐热门内容
                // 并且需要与页码相匹配,如果大于150我们就不需要在意下面的内容
                if (totalNum < 150 &&
                        totalNum < (newsListRequest.getRequestPageNumber() + 1) * 15){
                    Map param = new HashMap();
                    if ((newsListRequest.getRequestPageNumber() + 1) * 15 - totalNum <= 15 ){
                        param.put("offset", 0);
                        param.put("datanum", (newsListRequest.getRequestPageNumber() + 1) * 15 - totalNum);
                    } else {
                        param.put("offset", (newsListRequest.getRequestPageNumber() + 1) * 15 - totalNum);
                        param.put("datanum", 15);
                    }
                    List<Map> addivity = newsInfoImp.getHotDataForRemcom(param);
                    if (newsListData.size() == 0){
                        newsListData = addivity;
                    } else {
                        // 避免添加重复的数据
                        outer:
                        for (Map possible: addivity){
                            for (Map item: newsListData){
                                if (item.get("newsurl_id").toString().equals(possible.toString())){
                                    continue outer;
                                }
                            }
                            newsListData.add(possible);
                        }
                    }
                    totalNum = 150;
                }
                break;
            case "category":
                newsListData = newsInfoImp.getNewsListByCategory(newsListRequest);
                totalNum = newsInfoImp.getTotalNumByCategory(newsListRequest);
                break;
        }

        result.put("totalNum", totalNum);
        result.put("newsListData", newsListData);
        return result;
    }


}
