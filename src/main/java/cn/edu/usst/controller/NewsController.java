package cn.edu.usst.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("news")
public class NewsController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getNewsContent(){
        return "news";
    }


}
