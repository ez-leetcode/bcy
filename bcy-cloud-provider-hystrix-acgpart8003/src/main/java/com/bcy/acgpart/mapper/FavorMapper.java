package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.Favor;
import com.bcy.vo.CosForFavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FavorMapper extends BaseMapper<Favor> {

    @Select("SELECT b.username,b.photo,b.id,a.* FROM favor as a, user as b WHERE a.id = #{id} AND a.id = b.id ORDER BY a.create_time DESC")
    List<CosForFavor> getCosForFavor(@Param("id") Long id, Page<CosForFavor> page);

}
