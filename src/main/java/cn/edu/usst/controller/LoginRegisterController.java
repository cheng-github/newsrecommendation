package cn.edu.usst.controller;
import cn.edu.usst.model.User;
import cn.edu.usst.service.AuthenticationService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("portal")
public class LoginRegisterController {

    @Autowired
    AuthenticationService authenticationService;

    /**
     * 返回登录的界面，如果已经是登录转态转到新闻页面
     * @return
     */
    @RequestMapping("")
    public String showLogin(){
        Subject user = SecurityUtils.getSubject();
        if(user.getSession().getAttribute("login") == null)
            return "login";
        else
            return "redirect:/news.do";
    }

    /**
     * 返回注册请求的结果
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> register(@RequestBody User user, HttpServletRequest request, HttpServletResponse response){
        String userName = user.getUsername();
        String pwd = user.getPassword();
        return authenticationService.userRegister(userName, pwd);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> login(@RequestBody User loginData,HttpServletRequest request, HttpServletResponse response){
        Map result = new HashMap();
        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token =
                new UsernamePasswordToken(loginData.getUsername(), loginData.getPassword());
        try {
            user.login(token);
            result.put("msg", "登录成功");
            result.put("code", 1);
//            登录成功之后设置一个session值用于做区分,以及使用uuid作为数据标识的区分
            user.getSession().setAttribute("login", true);
        }catch (Exception e){
            result.put("msg","登录失败，请检查您的用户名密码是否正确");
            result.put("code", 0);
        }
        return result;
    }

}
