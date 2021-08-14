package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.CosPlay;
import com.bcy.vo.CosForFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CosPlayMapper extends BaseMapper<CosPlay> {

    @Select("SELECT a.id,a.number,a.description FROM cos_play as a AND fans as b WHERE b.from_id = #{id} AND a.id = b.to_id ORDER BY a.create_time DESC")
    List<CosForFollow> getFollowCosList(@Param("id") Long id, Page<CosForFollow> page);

}
