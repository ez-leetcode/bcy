package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.pojo.BlackUser;
import com.bcy.vo.BlackUserForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlackUserMapper extends BaseMapper<BlackUser> {

    @Select("SELECT b.id,b.username,b.photo FROM black_user as a , user as b WHERE b.id = a.black_id AND a.id = #{id} ORDER BY a.create_time DESC")
    List<BlackUserForList> getBlackList(@Param("id") Long id, Page<BlackUserForList> page);
}
