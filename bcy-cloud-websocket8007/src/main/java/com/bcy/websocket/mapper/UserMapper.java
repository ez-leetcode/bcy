package com.bcy.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.websocket.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
