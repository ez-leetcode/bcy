package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.QaFollow;
import com.bcy.vo.FollowQAForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QaFollowMapper extends BaseMapper<QaFollow> {

    @Select("SELECT b.* FROM qa_follow as a, user as b WHERE a.qa_number = #{number} AND a.id = b.id AND b.username LIKE concat('%',#{keyword},'%') ORDER BY a.create_time DESC")
    List<FollowQAForList> getFollowQAListByKeyWord(@Param("keyword") String keyword, @Param("number") Long number, Page<FollowQAForList> page);

    @Select("SELECT b.* FROM qa_follow as a, user as b WHERE a.qa_number = #{number} AND a.id = b.id ORDER BY a.create_time DESC")
    List<FollowQAForList> getFollowQAList(@Param("number") Long number, Page<FollowQAForList> page);

}
