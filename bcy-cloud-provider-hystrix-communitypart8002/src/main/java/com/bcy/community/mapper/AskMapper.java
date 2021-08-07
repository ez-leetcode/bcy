package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.pojo.Ask;
import com.bcy.vo.AskForAnswer;
import com.bcy.vo.AskForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AskMapper extends BaseMapper<Ask> {

    @Select("SELECT a.*, b.* FROM ask as a, user as b WHERE b.id = a.from_id AND a.to_id = #{id} AND a.answer is null ORDER BY a.create_time DESC")
    List<AskForAnswer> getWaitingAskList(@Param("id") Long id, Page<AskForAnswer> page);


    @Select("SELECT a.*, b.* FROM ask as a, user as b WHERE b.id = a.from_id AND a.to_id = #{id} ORDER BY a.create_time DESC")
    List<AskForList> getAskList(@Param("id") Long id, Page<AskForList> page);

}
