package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;

public interface TimeoutService {

    Result<JSONObject> timeoutHandler();

}
