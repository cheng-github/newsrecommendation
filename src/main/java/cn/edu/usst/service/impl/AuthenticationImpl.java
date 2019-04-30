package cn.edu.usst.service.impl;

import cn.edu.usst.mapper.AuthenticationMapper;
import cn.edu.usst.service.AuthenticationService;
import cn.edu.usst.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationImpl implements AuthenticationService {

    @Autowired
    AuthenticationMapper authenticationMapper;

    @Override
    public Map userRegister(String userName, String pwd) {
        Map param = new HashMap();
        Map result = new HashMap();
        param.put("username", userName);
        param.put("password", pwd);
        param.put("user_uuid", UUIDGenerator.getUUID());
        if (authenticationMapper.checkUserExist(param) >= 1){
            result.put("result", "fail");
            result.put("code", 0);
            return result;
        }else {
            authenticationMapper.addUser(param);
            result.put("result", "success");
            result.put("code", 1);
        }
        // 注册成功，返回注册成功消息
        return result;
    }

    @Override
    public String userLogin(String userName, String pwd) {
        Map param = new HashMap();
        param.put("username", userName);
        param.put("password", pwd);
        return authenticationMapper.userLogin(param);
    }
}
