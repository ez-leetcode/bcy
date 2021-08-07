package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.mapper.*;
import com.bcy.community.pojo.*;
import com.bcy.community.utils.RedisUtils;
import com.bcy.vo.UserAtForList;
import com.bcy.vo.UserCommentForList;
import com.bcy.vo.UserLikeForList;
import com.bcy.vo.UserNoReadCounts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private UserMessageMapper userMessageMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public JSONObject getAtMessageList(Long id, Long page, Long cnt, String keyword) {
        JSONObject jsonObject = new JSONObject();
        Page<UserAtForList> page1 = new Page<>(page,cnt);
        List<UserAtForList> userAtForList = atMessageMapper.getUserLikeList(id,page1);
        //获取具体信息待完成
        jsonObject.put("atList",userAtForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取用户at信息列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getLikeMessageList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<UserLikeForList> page1 = new Page<>(page,cnt);
        List<UserLikeForList> userLikeForList = likeMessageMapper.getUserLikeList(id,page1);
        jsonObject.put("likeList",userLikeForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取用户点赞信息列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getCommentMessageList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<UserCommentForList> page1 = new Page<>(page,cnt);
        List<UserCommentForList> userCommentForList = commentMessageMapper.getUserCommentList(id,page1);
        //获取具体信息待完成
        jsonObject.put("commentList",userCommentForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取用户评论信息列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getAllCounts(Long id) {
        JSONObject jsonObject = new JSONObject();
        UserMessage userMessage = new UserMessage();
        UserNoReadCounts userNoReadCounts = new UserNoReadCounts();
        String atCounts = redisUtils.getValue("atCounts_" + id);
        String commentCounts = redisUtils.getValue("commentCounts_" + id);
        String likeCounts = redisUtils.getValue("likeCounts_" + id);
        String messageCounts = redisUtils.getValue("messageCounts_" + id);
        if(atCounts == null || commentCounts == null || likeCounts == null || messageCounts == null){
            userMessage = userMessageMapper.selectById(id);
        }
        if(atCounts == null){
            userNoReadCounts.setAtCounts(userMessage.getAtCounts());
            redisUtils.saveByHoursTime("atCounts_" + id,userMessage.getAtCounts().toString(),12);
        }else{
            userNoReadCounts.setAtCounts(Integer.parseInt(atCounts));
        }
        if(commentCounts == null){
            userNoReadCounts.setCommentCounts(userMessage.getCommentCounts());
            redisUtils.saveByHoursTime("commentCounts_" + id,userMessage.getCommentCounts().toString(),12);
        }else{
            userNoReadCounts.setCommentCounts(Integer.parseInt(commentCounts));
        }
        if(likeCounts == null){
            userNoReadCounts.setLikeCounts(userMessage.getLikeCounts());
            redisUtils.saveByHoursTime("likeCounts_" + id,userMessage.getLikeCounts().toString(),12);
        }else{
            userNoReadCounts.setLikeCounts(Integer.parseInt(likeCounts));
        }
        if(messageCounts == null){
            userNoReadCounts.setMessageCounts(userMessage.getMessageCounts());
            redisUtils.saveByHoursTime("messageCounts_" + id,userMessage.getMessageCounts().toString(),12);
        }else{
            userNoReadCounts.setMessageCounts(Integer.parseInt(messageCounts));
        }
        jsonObject.put("userNoReadCounts",userNoReadCounts);
        log.info("获取用户未读计数信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public String allRead(Long id, Integer type) {
        if(type == 1){
            UpdateWrapper<AtMessage> wrapper = new UpdateWrapper<>();
            wrapper.eq("to_id",id)
                    .eq("is_read",0)
                    .set("is_read",1);
            //result为更新条数
            int result = atMessageMapper.update(new AtMessage(),wrapper);
            log.info("清空@未读成功，共清除：" + result + "条");
            redisUtils.saveByHoursTime("atCounts_" + id,String.valueOf(0),12);
        }else if(type == 2){
            UpdateWrapper<CommentMessage> wrapper = new UpdateWrapper<>();
            wrapper.eq("to_id",id)
                    .eq("is_read",0)
                    .set("is_read",1);
            int result = commentMessageMapper.update(new CommentMessage(),wrapper);
            log.info("清空评论未读成功，共清除：" + result + "条");
            redisUtils.saveByHoursTime("commentCounts_" + id,String.valueOf(0),12);
        }else if(type == 3){
            UpdateWrapper<LikeMessage> wrapper = new UpdateWrapper<>();
            wrapper.eq("to_id",id)
                    .eq("is_read",0)
                    .set("is_read",1);
            int result = likeMessageMapper.update(new LikeMessage(),wrapper);
            log.info("清空点赞未读成功，共清除：" + result + "条");
            redisUtils.saveByHoursTime("likeCounts_" + id,String.valueOf(0),12);
        }else{
            //待完成
            log.info("xxx");
        }
        log.info("用户未读信息全部已读成功");
        return "success";
    }

}
