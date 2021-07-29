package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.community.mapper.UserMapper;
import com.bcy.community.pojo.User;
import com.bcy.vo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HomePageServiceImpl implements HomePageService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getOthersInfo(Long id) {
        User user = userMapper.selectById(id);
        if(user == null){
            log.error("获取他人信息失败，用户不存在或已被封禁");
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInfo",new UserInfo(user.getId(),user.getUsername(),user.getPhoto(),user.getDescription(),user.getSex(),user.getFollowCounts(),user.getFansCounts()));
        log.info("获取他人用户信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}
