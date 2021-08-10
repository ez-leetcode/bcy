package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.acgpart.pojo.CosMonthHot;
import com.bcy.vo.CosForHot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CosMonthMapper extends BaseMapper<CosMonthHot> {

    @Select("SELECT a.cos_number,b.id FROM cos_month_hot as a AND cos as b WHERE a.create_time = #{time} AND a.cos_number = b.number ORDER BY a.rank ASC")
    List<CosForHot> getMonthHotList(@Param("time") String time);

}
