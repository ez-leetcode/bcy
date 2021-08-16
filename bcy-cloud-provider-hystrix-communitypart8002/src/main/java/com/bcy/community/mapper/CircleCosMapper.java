package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.community.pojo.CircleCos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CircleCosMapper extends BaseMapper<CircleCos> {

    @Select("SELECT a.circle_name FROM circle_cos as a WHERE a.cos_number = #{number} ORDER BY a.create_time DESC")
    List<String> getAllCircleNameFromCosNumber(@Param("number") Long number);

}
