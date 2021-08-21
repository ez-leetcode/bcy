package com.bcy.acgpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcy.acgpart.pojo.QaLabel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QaLabelMapper extends BaseMapper<QaLabel> {

    @Select("SELECT a.label FROM qa_label as a WHERE a.qa_number = #{number} ORDER BY a.create_time DESC")
    List<String> getAllCircleNameFromQaNumber(@Param("number") Long number);

}
