package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.mapper.UserMapper;
import com.bcy.community.pojo.User;
import com.bcy.vo.UserInfo;
import com.bcy.vo.UserInfoForSearchList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public JSONObject searchUser(Long id, Long page, Long cnt, String keyword) {
        JSONObject jsonObject = new JSONObject();
        Page<UserInfoForSearchList> page1 = new Page<>(page,cnt);
        List<UserInfoForSearchList> userInfoForSearchListList = userMapper.searchUser(id,keyword,page1);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("searchUserList",userInfoForSearchListList);
        log.info("搜索用户成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}
