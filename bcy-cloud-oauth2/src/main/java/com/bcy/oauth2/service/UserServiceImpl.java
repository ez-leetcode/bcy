package com.bcy.oauth2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.oauth2.mapper.UserLoginMapper;
import com.bcy.oauth2.mapper.UserMapper;
import com.bcy.oauth2.mapper.UserRoleMapper;
import com.bcy.oauth2.pojo.User;
import com.bcy.oauth2.pojo.UserLogin;
import com.bcy.oauth2.pojo.UserRole;
import com.bcy.oauth2.utils.JwtUtils;
import com.bcy.oauth2.utils.RedisUtils;
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
            userLoginMapper.insert(new UserLogin(null,phone,new BCryptPasswordEncoder().encode("lxm"),0,null,null));
            UserLogin userLogin1 = userLoginMapper.selectOne(wrapper);
            //添加用户权限
            userRoleMapper.insert(new UserRole(null,userLogin1.getId(),1,null,null));
            userMapper.insert(new User(userLogin1.getId(),phone,"女",null,null,null,null,null,0,0,0,0));
            token = JwtUtils.createToken(userLogin1.getId().toString(),userLogin1.getPassword());
            redisUtils.saveByHoursTime("token_" + userLogin1.getId().toString(),token,24);
        }else{
            //给token
            token = JwtUtils.createToken(userLogin.getId().toString(),userLogin.getPassword());
            //存入redis，24小时有效
            redisUtils.saveByHoursTime("token_" + userLogin.getId().toString(),token,24);
        }
        //验证码失效
        redisUtils.delete("1_" + phone);
        log.info("登录成功，用户");
        return token;
    }

}
