package cn.edu.usst.service.impl;

import cn.edu.usst.mapper.RecommendationMapper;
import cn.edu.usst.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AlgorithmServiceImp implements AlgorithmService {

    @Autowired
    RecommendationMapper recommendationMapper;

    @Override
    public List<Map> getUserClickData() {
        return recommendationMapper.getUserClickData();
    }

    @Override
    public Integer addRecomItem(Map map) {
        if (recommendationMapper.checkRecomItem(map) < 1){
            recommendationMapper.addRecomItem(map);
            return 1;
        }
        else
            return 0;
    }
}
