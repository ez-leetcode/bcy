package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.pojo.Cos;
import com.bcy.vo.CosForFollow;
import com.bcy.vo.CosForTopic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Mapper
public interface CosMapper extends BaseMapper<Cos> {

    @Select("SELECT a.id,a.number,a.description FROM cos as a AND fans as b WHERE b.from_id = #{id} AND a.id = b.to_id ORDER BY a.create_time DESC")
    List<CosForFollow> getFollowCosList(@Param("id") Long id, Page<CosForFollow> page);

}
