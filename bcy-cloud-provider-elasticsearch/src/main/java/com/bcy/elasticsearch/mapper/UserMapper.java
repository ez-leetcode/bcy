package com.bcy.elasticsearch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.elasticsearch.dto.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
