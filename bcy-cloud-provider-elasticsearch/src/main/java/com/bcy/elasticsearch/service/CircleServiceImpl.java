package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.utils.EsUtils;
import com.bcy.vo.SearchWordForJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class CircleServiceImpl implements CircleService{

    @Autowired
    private EsUtils esUtils;

    @Autowired
    private CosService cosService;

    @Override
    public JSONObject getRecommendCircle(Long id, Integer cnt,Integer page) {
        JSONObject jsonObject = new JSONObject();
        //获取关键词
        JSONObject jsonObject1 = cosService.getSearchKeywordList(id);
        SearchWordForJson searchWordForJson = JSONObject.parseObject(jsonObject1.toString(),SearchWordForJson.class);
        try {
            jsonObject = esUtils.RecommendCircle(searchWordForJson.getKeywordList(),cnt,page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("获取推荐圈子成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}