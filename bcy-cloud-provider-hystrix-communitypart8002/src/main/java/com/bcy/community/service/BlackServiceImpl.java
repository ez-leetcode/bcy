package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.mapper.BlackCircleMapper;
import com.bcy.community.mapper.BlackUserMapper;
import com.bcy.community.mapper.UserMapper;
import com.bcy.community.pojo.BlackCircle;
import com.bcy.community.pojo.BlackUser;
import com.bcy.vo.BlackCircleForList;
import com.bcy.vo.BlackUserForList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BlackServiceImpl implements BlackService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlackUserMapper blackUserMapper;

    @Autowired
    private BlackCircleMapper blackCircleMapper;

    @Override
    public String addBlack(Long id, Long blackId) {
        QueryWrapper<BlackUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("black_id",blackId);
        BlackUser blackUser = blackUserMapper.selectOne(wrapper);
        if(blackUser != null){
            log.error("添加黑名单失败，用户已被拉黑");
            return "repeatWrong";
        }
        //插入拉黑
        blackUserMapper.insert(new BlackUser(null,id,blackId,null));
        log.info("添加黑名单成功");
        return "success";
    }

    @Override
    public String deleteBlack(Long id, Long blackId) {
        QueryWrapper<BlackUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("black_id",blackId);
        BlackUser blackUser = blackUserMapper.selectOne(wrapper);
        if(blackUser == null){
            log.error("移除黑名单失败，用户未被拉黑");
            return "repeatWrong";
        }
        //移除拉黑
        blackUserMapper.deleteById(blackUser.getNumber());
        log.info("移除黑名单成功");
        return "success";
    }

    @Override
    public JSONObject getBlackList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<BlackUserForList> page1 = new Page<>(page,cnt);
        List<BlackUserForList> blackUserForListList = blackUserMapper.getBlackList(id,page1);
        jsonObject.put("blackList",blackUserForListList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取黑名单列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public JSONObject getBlackCircleList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        Page<BlackCircleForList> page1 = new Page<>(page,cnt);
        List<BlackCircleForList> blackCircleForListList = blackCircleMapper.getBlackCircleList(id,page1);
        jsonObject.put("blackCircleList",blackCircleForListList);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取拉黑圈子列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }



    @Override
    public String addBlackCircle(Long id, String circleName) {
        QueryWrapper<BlackCircle> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("circle_name",circleName);
        BlackCircle blackCircle = blackCircleMapper.selectOne(wrapper);
        if(blackCircle != null){
            log.error("拉黑圈子失败，圈子已被拉黑");
            return "repeatWrong";
        }
        //拉黑
        blackCircleMapper.insert(new BlackCircle(null,id,circleName,null));
        log.info("拉黑圈子成功");
        return "success";
    }

    @Override
    public String deleteBlackCircle(Long id, String circleName) {
        QueryWrapper<BlackCircle> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("circle_name",circleName);
        BlackCircle blackCircle = blackCircleMapper.selectOne(wrapper);
        if(blackCircle == null){
            log.error("取消拉黑圈子失败，圈子未被拉黑");
            return "repeatWrong";
        }
        //取消拉黑
        blackCircleMapper.deleteById(blackCircle);
        log.info("取消拉黑圈子成功");
        return "success";
    }

    @Override
    public JSONObject judgeBlack(Long id, Long toId) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<BlackUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("black_id",toId);
        BlackUser blackUser = blackUserMapper.selectOne(wrapper);
        if(blackUser == null){
            //未拉黑
            jsonObject.put("isBlack",0);
        }else{
            jsonObject.put("isBlack",1);
        }
        log.info("判断是否拉黑成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }

    @Override
    public JSONObject judgeBlackCircle(Long id, String circleName) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<BlackCircle> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id)
                .eq("circle_name",circleName);
        BlackCircle blackCircle = blackCircleMapper.selectOne(wrapper);
        if(blackCircle == null){
            jsonObject.put("isBlack",0);
        }else{
            jsonObject.put("isBlack",1);
        }
        log.info("判断是否拉黑圈子成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }
}
