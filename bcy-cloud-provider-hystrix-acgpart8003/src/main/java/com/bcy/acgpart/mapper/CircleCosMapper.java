package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.CircleCos;
import com.bcy.vo.CircleCosForList;
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

    @Select("SELECT b.number,b.id,b.description,b.create_time FROM circle_cos as a , cos_play as b WHERE a.circle_name = #{circleName} AND b.permission = 1 AND a.cos_number = b.number ORDER BY b.create_time DESC")
    List<CircleCosForList> getCircleCosListByTime(@Param("circleName") String circleName, Page<CircleCosForList> page);

    @Select("SELECT b.number,b.id,b.description,b.create_time FROM circle_cos as a , cos_play as b , cos_counts as c WHERE a.circle_name = #{circleName} AND b.permission = 1 AND a.cos_number = b.number AND c.number = b.number ORDER BY c.like_counts DESC")
    List<CircleCosForList> getCircleCosListByHot(@Param("circleName") String circleName, Page<CircleCosForList> page);

}
