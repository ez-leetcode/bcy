package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.community.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
