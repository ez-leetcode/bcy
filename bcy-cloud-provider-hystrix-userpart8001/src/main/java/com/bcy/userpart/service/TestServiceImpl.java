package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;
import com.bcy.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TestServiceImpl implements TestService{

    @Override
    public Result<JSONObject> getTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("test","我爱二次元");
        jsonObject.put("uuid", UUID.randomUUID().toString());
        return ResultUtils.getResult(jsonObject,"success");
    }

}
