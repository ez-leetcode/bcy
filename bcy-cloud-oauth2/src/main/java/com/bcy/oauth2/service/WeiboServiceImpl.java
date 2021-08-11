package com.bcy.oauth2.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.oauth2.config.WeiboConfig;
import com.bcy.oauth2.mapper.*;
import com.bcy.oauth2.pojo.*;
import com.bcy.oauth2.utils.JwtUtils;
import com.bcy.oauth2.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WeiboServiceImpl implements WeiboService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public String weiboLogin(String code) throws InterruptedException {
        Map<String,Object> map = new HashMap<>();
        map.put("client_id", WeiboConfig.client_id);
        map.put("client_secret",WeiboConfig.client_secret);
        map.put("grant_type",WeiboConfig.grant_type);
        map.put("redirect_uri",WeiboConfig.callBack_uri);
        map.put("code",code);
        String post = HttpUtil.post("https://api.weibo.com/oauth2/access_token",map);
        log.info("授权信息如下");
        log.info(post);
        Map<String,String> parse = (Map<String, String>) JSON.parse(post);
        String accessToken =  parse.get("access_token");
        log.info(accessToken);
        String uid =  parse.get("uid");
        log.info(uid);
        //查询是否有注册绑定过
        QueryWrapper<UserLogin> wrapper = new QueryWrapper<>();
        wrapper.eq("weibo_id",uid);
        UserLogin userLogin = userLoginMapper.selectOne(wrapper);
        String s = HttpUtil.get("https://api.weibo.com/2/users/show.json?uid=" + uid + "&access_token=" + accessToken);
        log.info(s);
        Map<String,String> parse1 = (Map<String, String>) JSON.parse(s);
        if(userLogin != null){
            log.info("微博账号已绑定");
            redisUtils.delete("3token_" + userLogin.getId().toString());
            String token = JwtUtils.createToken(userLogin.getId().toString(),userLogin.getPassword());
            redisUtils.saveByHoursTime("3token_" + userLogin.getId().toString(),token,24);
            return "b";
        }else{
            log.info("微博账号未绑定，是新用户");
            //创建用户
            UserLogin userLogin1 = new UserLogin(null,null,new BCryptPasswordEncoder().encode("lxm"),uid,0,null,null);
            userLoginMapper.insert(userLogin1);
            Thread.sleep(100);
            UserLogin userLogin2 = userLoginMapper.selectOne(wrapper);
            //添加用户权限
            userRoleMapper.insert(new UserRole(null,userLogin2.getId(),1,null,null));
            //插入用户基本信息
            String gender = parse1.get("gender");
            if(gender.equals("m")){
                gender = "男";
            }else{
                gender = "女";
            }
            userMapper.insert(new User(userLogin2.getId(),parse1.get("name"),gender,parse1.get("profile_image_url"),null,null,null,null,0,0,0,0));
            //插入用户设置
            userSettingMapper.insert(new UserSetting(userLogin2.getId(),1,1,1,1,1,0,null));
            //插入用户消息
            userMessageMapper.insert(new UserMessage(userLogin2.getId(),0,0,0,0,null,null));
            redisUtils.delete("3token_" + userLogin2.getId().toString());
            String token = JwtUtils.createToken(userLogin2.getId().toString(),userLogin2.getPassword());
            redisUtils.saveByHoursTime("3token_" + userLogin2.getId().toString(),token,24);
            return "a";
        }
    }

}
