package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TimeoutServiceImpl implements TimeoutService {

    @Override
    public Result<JSONObject> timeoutHandler() {
        //超时处理器
        return ResultUtils.getResult(new JSONObject(),"busyOrWrong");
    }

}
