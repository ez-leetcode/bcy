package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.Circle;
import com.bcy.vo.PersonalCircleForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CircleMapper extends BaseMapper<Circle> {

    @Select("SELECT a.circle_name, a.photo FROM circle as a, circle_follow as b WHERE b.circle_name = a.circle_name AND b.id = #{id} ORDER BY b.create_time DESC")
    List<PersonalCircleForList> getPersonalCircleList(@Param("id") Long id, Page<PersonalCircleForList> page);

}
