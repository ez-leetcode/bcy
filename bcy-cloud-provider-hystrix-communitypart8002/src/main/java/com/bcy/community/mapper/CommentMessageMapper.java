package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.community.pojo.CommentMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMessageMapper extends BaseMapper<CommentMessage> {
}
