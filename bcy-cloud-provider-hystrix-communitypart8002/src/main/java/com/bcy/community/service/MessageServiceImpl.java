package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.bcy.community.mapper.AtMessageMapper;
import com.bcy.community.mapper.CommentMessageMapper;
import com.bcy.community.mapper.LikeMessageMapper;
import com.bcy.community.mapper.UserMapper;
import com.bcy.community.pojo.LikeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService{

    @Autowired
    private AtMessageMapper atMessageMapper;

    @Autowired
    private CommentMessageMapper commentMessageMapper;

    @Autowired
    private LikeMessageMapper likeMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getAtMessageList(Long id, Long page, Long cnt, String keyword) {
        JSONObject jsonObject = new JSONObject();
        log.info("获取用户at信息列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getLikeMessageList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        log.info("获取用户点赞信息列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getCommentMessageList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        log.info("获取用户评论信息列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}
