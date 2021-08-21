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
        String s = searchWordForJson.getKeywordList();
        if(s == null || s.equals("[]")){
            s += "cos,绘画,写作";
        }
        try {
            log.info(s);
            jsonObject = esUtils.RecommendCircle(s,cnt,page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}