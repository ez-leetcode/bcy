package com.bcy.elasticsearch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.elasticsearch.dto.CircleFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CircleFollowMapper extends BaseMapper<CircleFollow> {

    @Select("SELECT a.circle_name FROM circle_follow as a WHERE a.id = #{id}")
    List<String> getCircleFollowList(@Param("id") Long id);

}
