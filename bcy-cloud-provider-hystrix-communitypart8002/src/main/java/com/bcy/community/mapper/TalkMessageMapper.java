package com.bcy.community.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.pojo.TalkMessage;
import com.bcy.vo.P2PTalkForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TalkMessageMapper extends BaseMapper<TalkMessage> {

    @Select("SELECT a.uu_id,a.from_id,a.to_id,a.create_time,a.message FROM talk_message as a WHERE a.from_id = #{id} AND a.to_id = #{toId} AND a.from_deleted = 0 OR a.to_id = #{id} AND a.from_id = #{toId} AND a.to_deleted = 0 ORDER BY a.create_time DESC")
    List<P2PTalkForList> getP2PTalkList(@Param("id") Long id, @Param("toId") Long toId, Page<P2PTalkForList> page);

}
