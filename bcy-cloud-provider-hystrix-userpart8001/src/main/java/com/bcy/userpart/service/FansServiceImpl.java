package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.config.RabbitmqConfig;
import com.bcy.mq.FansMsg;
import com.bcy.userpart.mapper.FansMapper;
import com.bcy.userpart.mapper.UserMapper;
import com.bcy.userpart.mapper.UserSettingMapper;
import com.bcy.userpart.pojo.Fans;
import com.bcy.userpart.pojo.User;
import com.bcy.userpart.pojo.UserSetting;
import com.bcy.userpart.utils.RedisUtils;
import com.bcy.vo.FansInfo;
import com.bcy.vo.FollowInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FansServiceImpl implements FansService{

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

    @Override
    public String addFollow(Long fromId, Long toId) {
        QueryWrapper<Fans> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",fromId)
                .eq("to_id",toId);
        Fans fans = fansMapper.selectOne(wrapper);
        if(fans != null){
            log.error("关注用户失败，用户已被关注");
            return "repeatWrong";
        }
        //添加关注
        fansMapper.insert(new Fans(null,fromId,toId,null));
        //修改数量
        redisUtils.addKeyByTime("fansCounts_" + toId,12);
        redisUtils.subKeyByTime("followCounts_" + fromId,12);
        //推送添加粉丝信息
        UserSetting userSetting = userSettingMapper.selectById(toId);
        if(userSetting != null && userSetting.getPushFans() == 1){
            //推送消息
            User user = userMapper.selectById(fromId);
            if(user != null){
                FansMsg fansMsg = new FansMsg(fromId,user.getUsername(),toId,new Date());
                rabbitmqProducerService.sendFansMsg(fansMsg);
            }
        }
        log.info("添加关注成功");
        return "success";
    }

    @Override
    public String deleteFollow(Long fromId, Long toId) {
        QueryWrapper<Fans> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",fromId)
                .eq("to_id",toId);
        Fans fans = fansMapper.selectOne(wrapper);
        if(fans == null){
            log.error("移除关注失败，用户未被关注");
            return "repeatWrong";
        }
        fansMapper.delete(wrapper);
        //修改数量
        redisUtils.subKeyByTime("fansCounts_" + toId,12);
        redisUtils.addKeyByTime("followCounts_" + fromId,12);
        log.info("取消关注成功");
        return "success";
    }

    @Override
    public String judgeFollow(Long fromId, Long toId) {
        QueryWrapper<Fans> wrapper = new QueryWrapper<>();
        wrapper.eq("from_id",fromId)
                .eq("to_id",toId);
        Fans fans = fansMapper.selectOne(wrapper);
        QueryWrapper<Fans> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("to_id",fromId)
                .eq("from_id",toId);
        Fans fans1 = fansMapper.selectOne(wrapper1);
        if(fans == null){
            log.info("用户未关注");
            return "0";
        }
        if(fans1 == null){
            log.info("用户已关注");
            return "1";
        }
        log.info("用户已相互关注");
        return "2";
    }

    @Override
    public JSONObject getFansList(Long id, String keyword, Long cnt, Long page) {
        Page<FansInfo> page1 = new Page<>(page,cnt);
        JSONObject jsonObject = new JSONObject();
        List<FansInfo> fansInfoList;
        if(keyword == null || keyword.equals("")){
            fansInfoList = fansMapper.getFansList(page1, id);
        }else{
            fansInfoList = fansMapper.getFansListByKeyword(page1, keyword, id);
        }
        for(FansInfo x:fansInfoList){
            String fansCounts = redisUtils.getValue("fansCounts_" + x.getId());
            if(fansCounts == null){
                redisUtils.saveByHoursTime("fansCounts_" + x.getId(),x.getFansCounts().toString(),12);
            }else{
                x.setFansCounts(Integer.parseInt(fansCounts));
            }
        }
        jsonObject.put("fansList",fansInfoList);
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("pages",page1.getPages());
        log.info("获取粉丝列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public JSONObject getFollowList(Long id, String keyword, Long cnt, Long page) {
        Page<FollowInfo> page1 = new Page<>(page,cnt);
        JSONObject jsonObject = new JSONObject();
        List<FollowInfo> followInfoList;
        if(keyword == null || keyword.equals("")){
            followInfoList = fansMapper.getFollowList(page1,id);
        }else{
            followInfoList = fansMapper.getFollowListByKeyword(page1,keyword,id);
        }
        for(FollowInfo x:followInfoList){
            String fansCounts = redisUtils.getValue("fansCounts_" + x.getId());
            if(fansCounts == null){
                redisUtils.saveByHoursTime("fansCounts_" + x.getId(),x.getFansCounts().toString(),12);
            }else{
                x.setFansCounts(Integer.parseInt(fansCounts));
            }
        }
        jsonObject.put("followList",followInfoList);
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("pages",page1.getPages());
        log.info("获取关注列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

}
