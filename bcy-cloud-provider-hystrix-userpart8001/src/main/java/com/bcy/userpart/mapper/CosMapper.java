package com.bcy.userpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.userpart.pojo.Cos;
import com.bcy.vo.CosHistoryForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CosMapper extends BaseMapper<Cos> {

    @Select("SELECT a.id,a.description,a.create_time,a.number FROM cos as a,history as b WHERE b.id = #{id} AND b.cos_number = a.number ORDER BY b.update_time DESC")
    List<CosHistoryForList> getCosHistoryForList(@Param("id") Long id, Page<CosHistoryForList> page);

}
