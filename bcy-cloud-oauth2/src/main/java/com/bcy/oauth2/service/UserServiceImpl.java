package com.bcy.oauth2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.oauth2.mapper.*;
import com.bcy.oauth2.pojo.*;
import com.bcy.oauth2.utils.JwtUtils;
import com.bcy.oauth2.utils.RedisUtils;
import com.bcy.utils.BloomFilterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private BloomFilterUtils bloomFilterUtils;

    @Override
    public String changePassword(String phone, String newPassword, String code) {
        String realCode = redisUtils.getValue("3_" + phone);
        if(realCode == null || !realCode.equals(code)){
            log.error("找回密码失败，验证码不存在或出错");
            return "codeWrong";
        }
        QueryWrapper<UserLogin> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        UserLogin userLogin = userLoginMapper.selectOne(wrapper);
        if(userLogin == null){
            log.error("找回密码失败，用户不存在");
            return "existWrong";
        }
        userLogin.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        //更新密码
        userLoginMapper.updateById(userLogin);
        log.info("找回密码成功");
        return "success";
    }

    @Override
    public String loginByCode(String phone, String code, Integer type){
        String realCode = redisUtils.getValue("1_" + phone);
        if(realCode == null || !realCode.equals(code)){
            log.error("登录失败，验证码错误");
            return "codeWrong";
        }
        QueryWrapper<UserLogin> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        UserLogin userLogin = userLoginMapper.selectOne(wrapper);
        String token;
        if(userLogin == null){
            //用户第一次登录，直接创号，待完成
            userLoginMapper.insert(new UserLogin(null,phone,new BCryptPasswordEncoder().encode("lxm"),null,0,null,null));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            UserLogin userLogin1 = userLoginMapper.selectOne(wrapper);
            //id添加布隆过滤器
            redisUtils.addByBloomFilter(bloomFilterUtils,"bcy",userLogin1.getId().toString());
            //添加用户权限
            userRoleMapper.insert(new UserRole(null,userLogin1.getId(),1,null,null));
            //插入用户基本信息
            userMapper.insert(new User(userLogin1.getId(),phone,"女",null,null,null,null,null,0,0,0,0));
            //插入用户设置
            userSettingMapper.insert(new UserSetting(userLogin1.getId(),1,1,1,1,1,0,null));
            //插入用户消息
            userMessageMapper.insert(new UserMessage(userLogin1.getId(),0,0,0,0,null,null));
            token = JwtUtils.createToken(userLogin1.getId().toString(),userLogin1.getPassword());
            redisUtils.saveByHoursTime(type + "token_" + userLogin1.getId().toString(),token,24);
        }else{
            //给token
            token = JwtUtils.createToken(userLogin.getId().toString(),userLogin.getPassword());
            //存入redis，24小时有效
            redisUtils.saveByHoursTime(type + "token_" + userLogin.getId().toString(),token,24);
        }
        //验证码失效
        redisUtils.delete("1_" + phone);
        log.info("登录成功，用户");
        return token;
    }


    @Override
    public String logout(Long id,Integer type) {
        //oauth2给的token在限定时限内是一致的，就不删了
        redisUtils.delete(type + "token_" + id);
        log.info("用户登出成功");
        return "success";
    }

}