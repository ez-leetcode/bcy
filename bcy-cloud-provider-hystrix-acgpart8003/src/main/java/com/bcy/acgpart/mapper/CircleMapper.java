package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.Circle;
import com.bcy.vo.CircleInfoForSearchList;
import com.bcy.vo.PersonalCircleForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CircleMapper extends BaseMapper<Circle> {

    @Select("SELECT a.circle_name, a.photo, a.description FROM circle as a, circle_follow as b WHERE b.circle_name = a.circle_name AND b.id = #{id} ORDER BY b.create_time DESC")
    List<PersonalCircleForList> getPersonalCircleList(@Param("id") Long id, Page<PersonalCircleForList> page);

    @Select("SELECT circle.circle_name,circle.photo,circle.description FROM circle WHERE circle.circle_name LIKE concat('%',#{keyword},'%') ORDER BY circle.follow_counts DESC")
    List<CircleInfoForSearchList> searchCircle(@Param("keyword") String keyword,Page<CircleInfoForSearchList> page);
}
