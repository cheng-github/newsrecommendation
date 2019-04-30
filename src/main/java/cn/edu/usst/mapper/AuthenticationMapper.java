package cn.edu.usst.mapper;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AuthenticationMapper {

    int checkUserExist(Map map);

    int addUser(Map map);

    String userLogin(Map map);
}
