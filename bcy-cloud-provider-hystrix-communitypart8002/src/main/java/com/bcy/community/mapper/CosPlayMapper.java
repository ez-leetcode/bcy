package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.pojo.CosPlay;
import com.bcy.vo.CosHomePageForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface CosPlayMapper extends BaseMapper<CosPlay> {

    @Select("SELECT b.username,b.photo,b.id,a.description,b.number,b.create_time FROM cos_play as a , user as b WHERE b.id = #{id} AND a.id = b.id ORDER BY a.create_time DESC")
    List<CosHomePageForList> getUserCosList(@Param("id") Long id, Page<CosHomePageForList> page);

}
