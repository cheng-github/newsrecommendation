package cn.edu.usst.service.impl;

import cn.edu.usst.mapper.ClickActionMapper;
import cn.edu.usst.service.ClickActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ClickActionImp implements ClickActionService {

    @Autowired
    ClickActionMapper clickActionMapper;

    @Override
    public int handleClickAction(Map map) {
        if (clickActionMapper.checkClickRecord(map) < 1){
            return clickActionMapper.addClickRecord(map);
        }else {
            return clickActionMapper.increaseClickTime(map);
        }
    }
}
