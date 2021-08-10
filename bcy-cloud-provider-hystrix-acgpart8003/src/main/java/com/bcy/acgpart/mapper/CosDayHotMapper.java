package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.acgpart.pojo.CosDayHot;
import com.bcy.vo.CosForHot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CosDayHotMapper extends BaseMapper<CosDayHot> {

    @Select("SELECT a.cos_number,b.id FROM cos_day_hot as a AND cos as b WHERE a.create_time = #{time} AND a.cos_number = b.number ORDER BY a.rank ASC")
    List<CosForHot> getDayHotList(@Param("time") String time);

}
