package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSONObject;

public interface UserService {

    JSONObject getRecommendUser(Long page,Long cnt);

}
