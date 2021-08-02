package com.bcy.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.oauth2.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
