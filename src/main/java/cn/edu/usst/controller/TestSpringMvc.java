package cn.edu.usst.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/springmvc")
public class TestSpringMvc {

    @RequestMapping("index")
    public String myTest(){
        return "testmvc";
    }

}
