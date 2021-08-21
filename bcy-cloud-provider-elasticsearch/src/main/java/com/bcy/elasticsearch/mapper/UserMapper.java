package com.bcy.elasticsearch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.elasticsearch.dto.User;
import com.bcy.vo.RecommendUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT a.id,a.username,a.photo FROM user as a ORDER BY a.fans_counts DESC")
    List<RecommendUser> getRecommendUserList(Page<RecommendUser> page);
}
