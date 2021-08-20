package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;

public interface CircleService {

    JSONObject getRecommendCircle(Long id,Integer cnt,Integer page);

}
