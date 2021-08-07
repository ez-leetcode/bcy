package com.bcy.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.pojo.CommentMessage;
import com.bcy.vo.UserCommentForList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMessageMapper extends BaseMapper<CommentMessage> {

    @Select("SELECT a.id,a.username,a.photo,b.type,b.cos_or_qa_number,b.type,b.is_read,b.create_time FROM user as a,comment_message as b WHERE a.id = b.to_id AND b.to_id = #{id} ORDER BY b.create_time")
    List<UserCommentForList> getUserCommentList(@Param("id") Long id, Page<UserCommentForList> page);

}
