package cn.edu.usst.service;




import java.util.Map;


public interface AuthenticationService {

    Map userRegister(String userName, String pwd);

    String userLogin(String userName, String pwd);
}
