package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.acgpart.pojo.CircleCos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CircleCosMapper extends BaseMapper<CircleCos> {

    @Select("SELECT a.circle_name FROM circle_cos as a WHERE a.cos_number = #{number} ORDER BY a.create_time DESC")
    List<String> getAllCircleNameFromCosNumber(@Param("number") Long number);

    @Select("SELECT a.number FROM circle_cos as a WHERE a.circle_name = #{circleName} AND a.cos_number = #{number}")
    Long judgeCircleCosExist(@Param("circleName") String circleName,@Param("number") Long number);
}
