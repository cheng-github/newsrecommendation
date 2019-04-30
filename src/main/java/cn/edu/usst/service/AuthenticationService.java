package cn.edu.usst.service;


import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AuthenticationService {

    Map userRegister(String userName, String pwd);

    String userLogin(String userName, String pwd);
}
