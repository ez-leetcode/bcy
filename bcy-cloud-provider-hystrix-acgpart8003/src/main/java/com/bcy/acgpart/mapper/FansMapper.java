package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.acgpart.pojo.Fans;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FansMapper extends BaseMapper<Fans> {

    @Select("SELECT a.from_id FROM fans as a WHERE a,to_id = #{id} ORDER BY a.create_time")
    List<Long> getAllFansId(@Param("id") Long id);

}
