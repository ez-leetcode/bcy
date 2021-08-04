package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.acgpart.mapper.CosCountsMapper;
import com.bcy.acgpart.mapper.CosMapper;
import com.bcy.acgpart.mapper.LikesMapper;
import com.bcy.acgpart.pojo.CosCounts;
import com.bcy.acgpart.pojo.Favor;
import com.bcy.acgpart.pojo.Likes;
import com.bcy.acgpart.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class LikeServiceImpl implements LikeService{

    //待会再说，暂时不清楚逻辑

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private CosCountsMapper cosCountsMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String addLike(Long id, Long number) {
        CosCounts cosCounts = cosCountsMapper.selectById(number);
        if(cosCounts == null){
            log.error("添加cos点赞失败，cos不存在");
            return "existWrong";
        }
        QueryWrapper<Likes> wrapper = new QueryWrapper<>();
        wrapper.eq("cos_number",number)
                .eq("id",id);
        Likes likes = likesMapper.selectOne(wrapper);
        if(likes != null){
            log.error("添加cos点赞失败，cos已被点赞");
            return "repeatWrong";
        }
        //添加likes
        likesMapper.insert(new Likes(null,number,id,null));
        //redis记录
        String ck = redisUtils.getValue("cosLikeCounts_" + number);
        if(ck == null){
            //redis存入
            redisUtils.saveByHoursTime("cosLikeCounts_" + number,cosCounts.getLikeCounts().toString(),12);
        }
        redisUtils.addKeyByTime("cosLikeCounts_" + number,12);
        log.info("添加cos点赞成功");
        return "success";
    }

    @Override
    public String deleteLike(Long id, Long number) {
        CosCounts cosCounts = cosCountsMapper.selectById(number);
        if(cosCounts == null){
            log.error("取消cos点赞失败，cos不存在");
            return "existWrong";
        }
        QueryWrapper<Likes> wrapper = new QueryWrapper<>();
        wrapper.eq("cos_number",number)
                .eq("id",id);
        Likes likes = likesMapper.selectOne(wrapper);
        if(likes == null){
            log.error("取消cos点赞失败，cos未被点赞");
            return "repeatWrong";
        }
        //删除likes
        likesMapper.deleteById(likes.getId());
        //redis记录
        String ck = redisUtils.getValue("cosLikeCounts_" + number);
        if(ck == null){
            //redis存入
            redisUtils.saveByHoursTime("cosLikeCounts_" + number,cosCounts.getLikeCounts().toString(),12);
        }
        redisUtils.subKeyByTime("cosLikeCounts_" + number,12);
        log.info("取消cos点赞成功");
        return "success";
    }

    //待修改
    @Override
    public JSONObject getLikeStatus(Long id, List<Long> number) {
        JSONObject jsonObject = new JSONObject();
        List<Integer> judgeLikeList = new LinkedList<>();
        for(Long x:number){
            QueryWrapper<Likes> wrapper = new QueryWrapper<>();
            wrapper.eq("cos_number",x)
                    .eq("id",id);
            Likes likes = likesMapper.selectOne(wrapper);
            if(likes == null){
                judgeLikeList.add(0);
            }else{
                judgeLikeList.add(1);
            }
        }
        jsonObject.put("judgeLikeList",judgeLikeList);
        log.info("获取点赞状态成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getLikeList(Long id, Long cnt, Long page) {
        JSONObject jsonObject = new JSONObject();
        log.info("正在获取喜欢列表，");
        return jsonObject;
    }



}