package com.bcy.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.websocket.pojo.TalkMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TalkMessageMapper extends BaseMapper<TalkMessage> {
}
