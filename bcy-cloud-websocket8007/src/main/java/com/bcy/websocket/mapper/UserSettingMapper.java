package com.bcy.websocket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.websocket.pojo.UserSetting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSettingMapper extends BaseMapper<UserSetting> {
}
