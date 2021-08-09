package com.bcy.oauth2.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.oauth2.mapper.RoleMapper;
import com.bcy.oauth2.mapper.UserLoginMapper;
import com.bcy.oauth2.mapper.UserRoleMapper;
import com.bcy.oauth2.pojo.Role;
import com.bcy.oauth2.pojo.UserLogin;
import com.bcy.oauth2.pojo.UserRole;
import com.bcy.oauth2.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    //同手机号要区别开
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("正在尝试登录-->loadUserByUsername");
        QueryWrapper<UserLogin> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",s);
        UserLogin userLogin = userLoginMapper.selectOne(wrapper);
        if(userLogin == null){
            log.error("登录失败，用户不存在");
            throw new UsernameNotFoundException("no user");
        }
        QueryWrapper<UserRole> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("id",userLogin.getId());
        Collection<GrantedAuthority> authList = new ArrayList<>();
        List<UserRole> userRoleList = userRoleMapper.selectList(wrapper1);
        log.info("用户拥有角色：" + userRoleList.toString());
        for(UserRole x:userRoleList){
            Role role = roleMapper.selectById(x.getRole());
            //权限赋予用户
            authList.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }
        log.info(authList.toString());
        return new User(userLogin.getPhone(),userLogin.getPassword(),authList);
    }

}