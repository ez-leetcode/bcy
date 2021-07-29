package com.bcy.userpart.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.vo.HelpMsgForGet;
import com.bcy.vo.HelpMsgForList;
import com.bcy.userpart.mapper.HelpsMapper;
import com.bcy.userpart.pojo.Helps;
import com.bcy.userpart.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class HelpServiceImpl implements HelpService{

    @Autowired
    private HelpsMapper helpsMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String addHelp(String question, String answer, Integer type) {
        helpsMapper.insert(new Helps(null,question,answer,type,0,0,null));
        log.info("添加用户帮助成功");
        return "success";
    }

    @Override
    public String deleteHelp(Long number) {
        int result = helpsMapper.deleteById(number);
        if(result == 0){
            log.error("移除用户帮助失败，该帮助不存在");
            return "existWrong";
        }
        log.info("移除用户帮助成功");
        return "success";
    }

    @Override
    public JSONObject getHelpList(Long cnt, Long page, Integer type) {
        QueryWrapper<Helps> wrapper = new QueryWrapper<>();
        wrapper.eq("type",type)
                .orderByDesc("create_time");
        Page<Helps> page1 = new Page<>(page,cnt);
        helpsMapper.selectPage(page1,wrapper);
        List<Helps> helpsList = page1.getRecords();
        List<HelpMsgForList> helpMsgForListList = new LinkedList<>();
        for(Helps x:helpsList){
            helpMsgForListList.add(new HelpMsgForList(x.getNumber(),x.getQuestion()));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("cnts",page1.getTotal());
        jsonObject.put("helpList",helpMsgForListList);
        log.info("获取帮助列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject getHelp(Long number) {
        Helps helps = helpsMapper.selectById(number);
        if(helps == null){
            log.error("获取帮助详细信息失败，帮助不存在");
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("help",new HelpMsgForGet(helps.getNumber(),helps.getQuestion(),helps.getAnswer()));
        log.info("获取帮助详细信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public String addSolve(Long number, Integer isSolved) {
        Helps helps = helpsMapper.selectById(number);
        if(helps == null) {
            log.error("添加解决情况失败，帮助编号不存在");
            return "existWrong";
        }
        //扔redis里，定时任务刷新
        if(isSolved == 1){
            redisUtils.addKeyByTime("isSolved_" + number.toString(),1);
        }else{
            redisUtils.addKeyByTime("noSolved_" + number.toString(),1);
        }
        log.info("添加解决情况成功");
        return "success";
    }

}