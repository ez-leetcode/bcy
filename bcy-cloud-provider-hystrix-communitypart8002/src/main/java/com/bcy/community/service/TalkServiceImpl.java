package com.bcy.community.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bcy.community.mapper.TalkMessageMapper;
import com.bcy.community.mapper.TalkUserMapper;
import com.bcy.community.mapper.UserMapper;
import com.bcy.community.pojo.TalkMessage;
import com.bcy.community.pojo.TalkUser;
import com.bcy.community.pojo.User;
import com.bcy.community.utils.RedisUtils;
import com.bcy.vo.JudgeOnlineForList;
import com.bcy.vo.P2PTalkForList;
import com.bcy.vo.TalkCountsForList;
import com.bcy.vo.TalkForList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class TalkServiceImpl implements TalkService{

    @Autowired
    private TalkMessageMapper talkMessageMapper;

    @Autowired
    private TalkUserMapper talkUserMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getJudgeOnline(List<Long> userId) {
        JSONObject jsonObject = new JSONObject();
        List<JudgeOnlineForList> judgeOnlineForListList = new LinkedList<>();
        for(Long x:userId){
            String ck = redisUtils.getValue("websocket_" + x);
            if(ck == null){
                //不在线
                judgeOnlineForListList.add(new JudgeOnlineForList(x,0));
            }else{
                //在线
                judgeOnlineForListList.add(new JudgeOnlineForList(x,1));
            }
        }
        jsonObject.put("judgeOnlineList",judgeOnlineForListList);
        log.info("获取用户在线信息成功");
        log.info(judgeOnlineForListList.toString());
        return jsonObject;
    }

    @Override
    public String deleteTalkMessage(Long id, List<Long> number) {
        for(Long x:number){
            TalkMessage talkMessage = talkMessageMapper.selectById(x);
            if(talkMessage != null){
                if(talkMessage.getFromId().equals(id)){
                    talkMessage.setFromDeleted(1);
                }else{
                    talkMessage.setToDeleted(1);
                }
                //更新删除标志
                talkMessageMapper.updateById(talkMessage);
            }
        }
        log.info("批量移除1对1聊天历史记录成功");
        return "success";
    }

    @Override
    public String deleteTalk(Long id, Long toId) {
        QueryWrapper<TalkUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id1",id)
                .eq("id2",toId)
                .or()
                .eq("id1",toId)
                .eq("id2",id);
        TalkUser talkUser = talkUserMapper.selectOne(wrapper);
        if(talkUser == null){
            log.error("删除聊天失败，聊天不存在");
            return "existWrong";
        }
        if(talkUser.getId1().equals(id)){
            talkUser.setId1Deleted(1);
        }else{
            talkUser.setId2Deleted(1);
        }
        talkUserMapper.updateById(talkUser);
        log.info("删除用户聊天记录成功");
        return "success";
    }

    @Override
    public JSONObject getTalkCounts(Long id, List<Long> toId) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        List<TalkCountsForList> talkCountsForListList = new LinkedList<>();
        for(Long x:toId){
            TalkCountsForList talkCountsForList = new TalkCountsForList();
            talkCountsForList.setId(x);
            //获取最后一次消息
            String lastInfo = redisUtils.getUserLastTalk(id,x);
            //最新更新时间
            Date lastTime = redisUtils.getUserLastTime(id,x);
            //未读条数
            String noReadCounts = redisUtils.getValue("noReadTalk_" + x + id);
            TalkUser talkUser = new TalkUser();
            if(lastInfo == null || lastTime == null || noReadCounts == null){
                //信息有还未缓存
                QueryWrapper<TalkUser> wrapper = new QueryWrapper<>();
                wrapper.eq("id1",id)
                        .eq("id2",x)
                        .or()
                        .eq("id1",x)
                        .eq("id2",id);
                talkUser = talkUserMapper.selectOne(wrapper);
            }
            if(lastInfo != null){
                talkCountsForList.setLastInfo(lastInfo);
            }else{
                if(talkUser != null){
                    talkCountsForList.setLastInfo(talkUser.getLastTalk());
                    //同步到redis
                    redisUtils.saveByHoursTime("lastInfo_" + id + x,talkUser.getLastTalk(),48);
                }
            }
            if(lastTime != null){
                talkCountsForList.setUpdateTime(lastTime);
            }else{
                if(talkUser != null){
                    talkCountsForList.setUpdateTime(talkUser.getUpdateTime());
                    //同步到redis
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    redisUtils.saveByHoursTime("updateTime_" + id + x,format.format(talkUser.getUpdateTime()),48);
                }
            }
            if(noReadCounts != null){
                talkCountsForList.setNoReadCounts(Integer.parseInt(noReadCounts));
            }else{
                if(talkUser != null){
                    if(talkUser.getId1().equals(id)){
                        talkCountsForList.setNoReadCounts(talkUser.getId1Read());
                        redisUtils.saveByHoursTime("noReadTalk_" + x + id,talkUser.getId1Read().toString(),48);
                    }else{
                        talkCountsForList.setNoReadCounts(talkUser.getId2Read());
                        redisUtils.saveByHoursTime("noReadTalk_" + x + id,talkUser.getId2Read().toString(),48);
                    }
                }
            }
            talkCountsForListList.add(talkCountsForList);
        }
        jsonObject.put("talkCountsList",talkCountsForListList);
        log.info("获取聊天计数信息成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public JSONObject getTalkList(Long id, Long page, Long cnt) {
        JSONObject jsonObject = new JSONObject();
        QueryWrapper<TalkUser> wrapper = new QueryWrapper<>();
        wrapper.eq("id1",id)
                .eq("id1_deleted",0)
                .orderByDesc("update_time")
                .or()
                .eq("id2",id)
                .eq("id2_deleted",0)
                .orderByDesc("update_time");
        Page<TalkUser> page1 = new Page<>(page,cnt);
        talkUserMapper.selectPage(page1,wrapper);
        List<TalkUser> talkUserList = page1.getRecords();
        log.info(page1.getRecords().toString());
        List<TalkForList> talkForLists = new LinkedList<>();
        for(TalkUser x:talkUserList){
            User user;
            if(x.getId1().equals(id)){
                user = userMapper.selectById(x.getId2());
            }else{
                user = userMapper.selectById(x.getId1());
            }
            talkForLists.add(new TalkForList(user.getId(),user.getUsername(),user.getPhoto()));
        }
        jsonObject.put("talkList",talkForLists);
        jsonObject.put("pages",page1.getPages());
        jsonObject.put("counts",page1.getTotal());
        log.info("获取聊天列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public JSONObject getP2PTalkList(Long id, Long toId, Long cnt, Long page) {
        JSONObject jsonObject = new JSONObject();
        Page<P2PTalkForList> page1 = new Page<>(page,cnt);
        List<P2PTalkForList> p2PTalkForLists = talkMessageMapper.getP2PTalkList(id,toId,page1);
        jsonObject.put("p2pTalkList",p2PTalkForLists);
        jsonObject.put("counts",page1.getTotal());
        jsonObject.put("pages",page1.getPages());
        log.info("获取一对一聊天列表成功");
        log.info(jsonObject.toString());
        return jsonObject;
    }


    @Override
    public String allRead(Long id, Long toId) {
        redisUtils.saveByHoursTime("noReadTalk_" + toId + id,"0",48);
        log.info("添加全部已读成功");
        return "success";
    }


}