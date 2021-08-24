package com.bcy.userpart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.userpart.pojo.QaHistory;
import com.bcy.vo.QaHistoryForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QaHistoryMapper extends BaseMapper<QaHistory> {

    @Select("SELECT b.id,b.number,b.description,b.title,b.create_time FROM qa_history as a , qa as b WHERE b.number = a.qa_number AND a.id = #{id} ORDER BY a.update_time DESC")
    List<QaHistoryForList> getQaHistory(@Param("id") Long id, Page<QaHistoryForList> page);

}
