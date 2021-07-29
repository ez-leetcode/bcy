package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.userpart.mapper.FansMapper;
import com.bcy.userpart.pojo.Fans;
import com.bcy.userpart.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FansServiceImpl implements FansService{

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FansMapper fansMapper;

    @Override
    public String addFollow(Long fromId, Long toId) {
        return null;
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
        //修改数量待完成
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
        return null;
    }


    @Override
    public JSONObject getFollowList(Long id, String keyword, Long cnt, Long page) {
        return null;
    }

}
