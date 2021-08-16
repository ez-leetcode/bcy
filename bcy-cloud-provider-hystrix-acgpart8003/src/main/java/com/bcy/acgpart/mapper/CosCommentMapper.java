package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.CosComment;
import com.bcy.vo.CosCommentForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CosCommentMapper extends BaseMapper<CosComment> {

    @Select("SELECT b.number,a.id,a.username,a.photo,b.description,b.create_time FROM user as a, cos_comment as b WHERE b.cos_number = #{number} AND a.id = b.from_id AND b.father_number = 0 ORDER BY b.like_counts DESC")
    List<CosCommentForList> getCosCommentListByHot(@Param("number") Long number, Page<CosCommentForList> page);

    @Select("SELECT b.number,a.id,a.username,a.photo,b.description,b.create_time FROM user as a, cos_comment as b WHERE b.cos_number = #{number} AND a.id = b.from_id AND b.father_number = 0 ORDER BY b.create_time DESC")
    List<CosCommentForList> getCosCommentListByTime(@Param("number") Long number, Page<CosCommentForList> page);

    //@Select("SELECT FROM ")
    //List<CosCommentCommentForList> getCommentComment(@Param("number") Long number, Page<CosCommentCommentForList> page);

}
