package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.pojo.BlackCircle;
import com.bcy.vo.BlackCircleForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlackCircleMapper extends BaseMapper<BlackCircle> {

    @Select("SELECT a.circle_name,a.description,a.photo,b.create_time FROM circle as a , black_circle as b WHERE a.circle_name = b.circle_name AND b.id = #{id} ORDER BY b.create_time DESC")
    List<BlackCircleForList> getBlackCircleList(@Param("id") Long id, Page<BlackCircleForList> page);

}
