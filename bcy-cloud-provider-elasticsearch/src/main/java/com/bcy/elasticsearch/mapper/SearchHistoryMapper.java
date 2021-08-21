package com.bcy.elasticsearch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.elasticsearch.dto.SearchHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {

    @Select("SELECT a.keyword FROM search_history as a WHERE a.id = #{id} AND a.deleted = 0 ORDER BY a.update_time DESC")
    List<String> getHistoryKeywordList(@Param("id") Long id, Page<String> page);

}
