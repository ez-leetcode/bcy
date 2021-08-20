package com.bcy.elasticsearch.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bcy.elasticsearch.mapper.UserMapper;
import com.bcy.elasticsearch.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public JSONObject getRecommendUser() {
        JSONObject jsonObject = new JSONObject();
        log.info("获取推荐用户成功");
        return jsonObject;
    }
}
