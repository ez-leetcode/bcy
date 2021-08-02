package com.bcy.acgpart.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.acgpart.mapper.QaFollowMapper;
import com.bcy.acgpart.mapper.QaMapper;
import com.bcy.acgpart.pojo.Qa;
import com.bcy.acgpart.pojo.QaFollow;
import com.bcy.acgpart.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QAServiceImpl implements QAService{

    @Autowired
    private QaMapper qaMapper;

    @Autowired
    private QaFollowMapper qaFollowMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String followQA(Long id, Long number) {
        Qa qa = qaMapper.selectById(number);
        if(qa == null){
            log.error("关注问答失败，问答不存在");
            return "existWrong";
        }
        QueryWrapper<QaFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("qa_number",number)
                .eq("id",id);
        QaFollow qaFollow = qaFollowMapper.selectOne(wrapper);
        if(qaFollow != null){
            log.error("关注问题失败，问题已被关注");
            return "repeatWrong";
        }
        //插入记录
        qaFollowMapper.insert(new QaFollow(null,number,id,null));
        //问答关注数加1，等定时调度
        redisUtils.addKeyByTime("followQA_" + number,12);
        log.info("关注问答成功");
        return "success";
    }

    @Override
    public String disFollowQA(Long id, Long number) {
        Qa qa = qaMapper.selectById(number);
        if(qa == null){
            log.error("取消关注问答失败，问答不存在");
            return "existWrong";
        }
        QueryWrapper<QaFollow> wrapper = new QueryWrapper<>();
        wrapper.eq("qa_number",number)
                .eq("id",id);
        QaFollow qaFollow = qaFollowMapper.selectOne(wrapper);
        if(qaFollow == null){
            log.error("取消关注问题失败，问题已被关注");
            return "repeatWrong";
        }
        //插入记录
        qaFollowMapper.deleteById(qaFollow);
        //问答关注数减1，等定时调度
        redisUtils.subKeyByTime("followQA_" + number,12);
        log.info("取消关注问答成功");
        return "success";
    }
}
