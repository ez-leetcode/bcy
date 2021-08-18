package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.Likes;
import com.bcy.vo.CosLikeForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LikesMapper extends BaseMapper<Likes> {

    @Select("SELECT b.number,b.id,b.description,b.create_time FROM likes as a, cos_play as b WHERE a.id = #{id} AND a.cos_number = b.number ORDER BY b.create_time DESC")
    List<CosLikeForList> getCosLikeForList(@Param("id") Long id, Page<CosLikeForList> page);

}
