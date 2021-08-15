package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.pojo.User;
import com.bcy.vo.UserInfoForSearchList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT user.id,user.username,user.photo FROM user WHERE user.id != #{id} AND user.username LIKE concat('%',#{keyword},'%') ORDER BY user.fans_counts DESC")
    List<UserInfoForSearchList> searchUser(@Param("id") Long id, @Param("keyword") String keyword, Page<UserInfoForSearchList> page);

}
