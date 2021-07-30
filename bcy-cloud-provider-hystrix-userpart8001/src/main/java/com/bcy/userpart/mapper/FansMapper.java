package com.bcy.userpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.userpart.pojo.Fans;
import com.bcy.vo.FansInfo;
import com.bcy.vo.FollowInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FansMapper extends BaseMapper<Fans> {

    @Select("SELECT a.* FROM user a, fans b WHERE b.to_id = #{id} AND b.from_id = a.id AND a.username LIKE concat('%',#{keyword},'%') ORDER BY b.create_time")
    List<FansInfo> getFansListByKeyword(Page<FansInfo> page,@Param("keyword")String keyword,@Param("id")Long id);

    @Select("SELECT a.* FROM user a, fans b WHERE b.to_id = #{id} AND b.from_id = a.id ORDER BY b.create_time")
    List<FansInfo> getFansList(Page<FansInfo> page,@Param("id") Long id);

    @Select("SELECT a.* FROM user a, fans b WHERE b.from_id = #{id} AND b.to_id = a.id AND a.username LIKE concat('%',#{keyword},'%') ORDER BY b.create_time")
    List<FollowInfo> getFollowListByKeyword(Page<FollowInfo> page, @Param("keyword")String keyword, @Param("id")Long id);

    @Select("SELECT a.* FROM user a, fans b WHERE b.from_id = #{id} AND b.to_id = a.id ORDER BY b.create_time")
    List<FollowInfo> getFollowList(Page<FollowInfo> page, @Param("id")Long id);

}
