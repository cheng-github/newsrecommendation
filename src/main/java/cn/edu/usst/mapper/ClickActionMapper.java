package cn.edu.usst.mapper;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ClickActionMapper {

    int addClickRecord(Map map);

    int checkClickRecord(Map map);

    int increaseClickTime(Map map);
}
