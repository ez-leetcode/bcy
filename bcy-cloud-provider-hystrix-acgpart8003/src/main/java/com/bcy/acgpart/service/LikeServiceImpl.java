package com.bcy.acgpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.acgpart.mapper.*;
import com.bcy.acgpart.pojo.*;
import com.bcy.acgpart.utils.RedisUtils;
import com.bcy.mq.LikeMsg;
import com.bcy.utils.PhotoUtils;
import com.bcy.vo.CosJudgeLikeForList;
import com.bcy.vo.CosLikeForList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class LikeServiceImpl implements LikeService{

    @Autowired
    private LikesMapper likesMapper;

    @Autowired
    private CosCountsMapper cosCountsMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CosPlayMapper cosPlayMapper;

    @Autowired
    private CircleCosMapper circleCosMapper;

    @Autowired
    private LikeMessageMapper likeMessageMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

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
        Long cosExist = circleCosMapper.judgeCircleCosExist("cos",number);
        Long drawExist = circleCosMapper.judgeCircleCosExist("绘画",number);
        Long writeExist = circleCosMapper.judgeCircleCosExist("写作",number);
        if(cosExist != null){
            redisUtils.addKeyByTime("cosHotLikeCounts1_" + number,48);
            redisUtils.addKeyByTime("cosHotLikeWeekCounts1_" + number,24 * 8);
        }
        if(drawExist != null){
            redisUtils.addKeyByTime("cosHotLikeCounts2_" + number,48);
            redisUtils.addKeyByTime("cosHotLikeWeekCounts2_" + number,24 * 8);
        }
        if(writeExist != null){
            redisUtils.addKeyByTime("cosHotLikeCounts3_" + number,48);
            redisUtils.addKeyByTime("cosHotLikeWeekCounts3_" + number,24 * 8);
        }
        //websocket推送
        CosPlay cosPlay = cosPlayMapper.selectById(number);
        User user = userMapper.selectById(id);
        if(cosPlay != null && user != null){
            rabbitmqProducerService.sendLikeMessage(new LikeMsg(number,1,user.getUsername(), cosPlay.getId()));
            //插入点赞消息
            likeMessageMapper.insert(new LikeMessage(null,id,cosPlay.getId(),cosPlay.getNumber(),1,0,null));
            //更新点赞次数
            String ck1 = redisUtils.getValue("likeCounts_" + cosPlay.getId());
            if(ck1 == null){
                UserMessage userMessage = userMessageMapper.selectById(cosPlay.getId());
                if(userMessage != null){
                    redisUtils.saveByHoursTime("likeCounts_" + cosPlay.getId(),userMessage.getLikeCounts().toString(),12);
                }
                redisUtils.addKeyByTime("likeCounts_" + cosPlay.getId(),12);
            }
        }
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
        likesMapper.deleteById(likes.getNumber());
        //redis记录
        String ck = redisUtils.getValue("cosLikeCounts_" + number);
        if(ck == null){
            //redis存入
            redisUtils.saveByHoursTime("cosLikeCounts_" + number,cosCounts.getLikeCounts().toString(),12);
        }
        Long cosExist = circleCosMapper.judgeCircleCosExist("cos",number);
        Long drawExist = circleCosMapper.judgeCircleCosExist("绘画",number);
        Long writeExist = circleCosMapper.judgeCircleCosExist("写作",number);
        if(cosExist != null){
            redisUtils.subKeyByTime("cosHotLikeCounts1_" + number,48);
            redisUtils.subKeyByTime("cosHotLikeWeekCounts1_" + number,24 * 8);
        }
        if(drawExist != null){
            redisUtils.subKeyByTime("cosHotLikeCounts2_" + number,48);
            redisUtils.subKeyByTime("cosHotLikeWeekCounts2_" + number,24 * 8);
        }
        if(writeExist != null){
            redisUtils.subKeyByTime("cosHotLikeCounts3_" + number,48);
            redisUtils.subKeyByTime("cosHotLikeWeekCounts3_" + number,24 * 8);
        }
        redisUtils.subKeyByTime("cosLikeCounts_" + number,12);
        log.info("取消cos点赞成功");
        return "success";
    }

    //待修改
    @Override
    public JSONObject getLikeStatus(Long id, List<Long> number) {
        JSONObject jsonObject = new JSONObject();
        List<CosJudgeLikeForList> judgeLikeList = new LinkedList<>();
        for(Long x:number){
            QueryWrapper<Likes> wrapper = new QueryWrapper<>();
            wrapper.eq("cos_number",x)
                    .eq("id",id);
            Likes likes = likesMapper.selectOne(wrapper);
            if(likes == null){
                judgeLikeList.add(new CosJudgeLikeForList(x,0));
            }else{
                judgeLikeList.add(new CosJudgeLikeForList(x,1));
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
        Page<CosLikeForList> page1 = new Page<>(page,cnt);
        List<CosLikeForList> cosLikeForList = likesMapper.getCosLikeForList(id,page1);
        for(CosLikeForList x:cosLikeForList){
            User user = userMapper.selectById(x.getId());
            if(user != null){
                x.setUsername(user.getUsername());
                x.setPhoto(user.getPhoto());
            }
            CosPlay cosPlay = cosPlayMapper.selectById(x.getNumber());
            if(cosPlay != null){
                x.setCosPhoto(PhotoUtils.photoStringToList(cosPlay.getPhoto()));
                x.setCreateTime(cosPlay.getCreateTime());
            }
        }
        jsonObject.put("likeCosList",cosLikeForList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取喜欢列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}