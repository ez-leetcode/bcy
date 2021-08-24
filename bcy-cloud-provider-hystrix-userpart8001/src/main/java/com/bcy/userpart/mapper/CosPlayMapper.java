package com.bcy.userpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.userpart.pojo.CosPlay;
import com.bcy.vo.CosHistoryForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CosPlayMapper extends BaseMapper<CosPlay> {

    @Select("SELECT a.id,a.description,a.create_time,a.number FROM cos_play as a,history as b WHERE b.id = #{id} AND b.cos_number = a.number ORDER BY b.update_time DESC")
    List<CosHistoryForList> getCosHistoryForList(@Param("id") Long id, Page<CosHistoryForList> page);

}
