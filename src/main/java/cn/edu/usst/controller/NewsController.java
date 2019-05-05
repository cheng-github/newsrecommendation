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
        }

        result.put("totalNum", totalNum);
        result.put("newsListData", newsListData);
        return result;
    }


}
