package com.bcy.websocket.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcy.mq.*;
import com.bcy.pojo.SystemInfo;
import com.bcy.utils.WebsocketResultUtils;
import com.bcy.websocket.mapper.*;
import com.bcy.websocket.pojo.*;
import com.bcy.websocket.utils.RedisUtils;
import com.bcy.websocket.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

//总线上的连接
@Component
@Slf4j
@ServerEndpoint(value = "/websocket/{id}")
public class WebsocketService {

    @Resource
    private RedisUtils redisUtils = SpringUtils.getBean(RedisUtils.class);

    @Autowired
    private RabbitmqProducerService rabbitmqProducerService;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private TalkUserMapper talkUserMapper;

    @Autowired
    private TalkMessageMapper talkMessageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlackUserMapper blackUserMapper;

    //与某个客户端连接会话，以此来给客户端发送数据
    private Session session;

    //线程安全hashmap，存放每个客户端对应的websocket对象
    private static ConcurrentHashMap<String,WebsocketService> websocketServiceConcurrentHashMap = new ConcurrentHashMap<>();

    //建立连接会调用这个方法
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id){
        log.info("正在建立与app的连接，用户id：" + id);
        //建立连接
        this.session = session;
        //放入hashmap中
        websocketServiceConcurrentHashMap.put(id,this);
        //登录信息存入redis
        redisUtils.saveByHoursTime("websocket_" + id,"1",999);
        log.info("有新的连接建立，当前总连接数：" + websocketServiceConcurrentHashMap.size());
    }

    //关闭连接时调用这个方法
    @OnClose
    public void onClose(@PathParam("id") String id){
        log.info("正在关闭与app的连接，用户：" + id);
        websocketServiceConcurrentHashMap.remove(id);
        //登录信息移除
        redisUtils.delete("websocket_" + id);
        log.info("连接已断开，当前连接数：" + websocketServiceConcurrentHashMap.size());
    }

    //聊天出错时调用
    @OnError
    public void onEorror(Session session, Throwable error){
        log.info("app连接出现错误：" + session.getId());
        error.printStackTrace();
    }

    //有消息从客户端发送进来，发给消息队列
    @OnMessage
    public void onMessage(Session session,String message){
        log.info("有消息从客户端发送进来");
        log.info(message);
        log.info(session.toString());
        //把message转成talkMsg
        //rabbitmqWebsocketProductConfig.sendMessageToFanoutExchange(message);
        TalkMsg talkMsg = JSON.parseObject(message,TalkMsg.class);
        rabbitmqProducerService.sendTalkMsg(talkMsg);
        log.info("rabbitmq发送信息成功");
    }


    public void getTalk(TalkMsg talkMsg){
        log.info("正在消费聊天消息");
        WebsocketService websocketService = websocketServiceConcurrentHashMap.get(talkMsg.getToId().toString());
        if(websocketService != null){
            UserSetting userSetting = userSettingMapper.selectById(talkMsg.getToId());
            if(userSetting != null && userSetting.getPushInfo() == 1){
                //非免打扰直接推送
                User user = userMapper.selectById(talkMsg.getFromId());
                SystemInfo systemInfo = new SystemInfo(user.getUsername(),talkMsg.getMsg());
                websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"talkInfo",talkMsg.getFromId().toString()).toString());
                log.info("聊天推送成功");
            }
            //查询是否被拉黑
            QueryWrapper<BlackUser> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("id",talkMsg.getToId())
                    .eq("black_id",talkMsg.getFromId());
            BlackUser blackUser = blackUserMapper.selectOne(wrapper1);
            if(blackUser == null){
                //未被屏蔽
                //插入聊天列表
                QueryWrapper<TalkUser> wrapper = new QueryWrapper<>();
                wrapper.eq("id1",talkMsg.getFromId())
                        .eq("id2",talkMsg.getToId())
                        .or()
                        .eq("id1",talkMsg.getToId())
                        .eq("id2",talkMsg.getFromId());
                TalkUser talkUser = talkUserMapper.selectOne(wrapper);
                if(talkUser == null){
                    //新建聊天
                    talkUserMapper.insert(new TalkUser(talkMsg.getFromId(),talkMsg.getToId(),0,1,0,0,talkMsg.getMsg(),null,null));
                }else{
                    //更新时间
                    talkUser.setUpdateTime(new Date());
                    talkUserMapper.updateById(talkUser);
                    //redis更新
                    redisUtils.updateTalkTime(talkMsg.getFromId(),talkMsg.getToId());
                    //更新最后聊天的信息，不直接更新 定期更新回mysql
                    redisUtils.updateTalkInfo(talkMsg.getFromId(),talkMsg.getToId(),talkMsg.getMsg());
                }
                //插入聊天
                talkMessageMapper.insert(new TalkMessage(null,talkMsg.getFromId(),talkMsg.getToId(),talkMsg.getMsg(),talkMsg.getUuId(),0,0,null));
                //如果websocket实例在这个服务器上，保存聊天记录并返回ack，可能不行！！！！还是要广播消息队列
                //把uuid当ack发回去
                rabbitmqProducerService.sendAckMsg(new TalkAckMsg(talkMsg.getFromId(),talkMsg.getUuId(),1));

            }else{
                //把被屏蔽的消息发回去
                rabbitmqProducerService.sendAckMsg(new TalkAckMsg(talkMsg.getFromId(),talkMsg.getUuId(),0));
            }
            log.info("聊天信息消费成功");
        }
    }

    public void sendAckMessage(TalkAckMsg talkAckMsg){
        log.info("正在向在线用户推送对应的ack消息");
        //这个不推送到页面上，不用检查免打扰
        WebsocketService websocketService = websocketServiceConcurrentHashMap.get(talkAckMsg.getId().toString());
        if(websocketService != null){
            log.info("正在返回ack");
            websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(talkAckMsg.toString()),"talkReceive",null).toString());
            log.info("ack返回成功");
        }
    }


    public void sendFansMessage(FansMsg fansMsg){
        log.info("正在向别的在线用户推送粉丝添加");
        //免打扰在上游处理过了
        WebsocketService websocketService = websocketServiceConcurrentHashMap.get(fansMsg.getToId().toString());
        if(websocketService != null){
            log.info("正在向用户" + websocketService.session.getId() + "推送粉丝添加信息");
            SystemInfo systemInfo = new SystemInfo("您有新的粉丝关注","用户" + fansMsg.getFromUsername() + "关注了您");
            websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"fansInfo", fansMsg.getFromId().toString()).toString());
            log.info("粉丝添加推送成功");
        }
    }

    public void sendAtMessage(AtMsg atMsg){
        log.info("正在向别的在线用户推送@");
        WebsocketService websocketService = websocketServiceConcurrentHashMap.get(atMsg.getToId().toString());
        if(websocketService != null){
            //免打扰处理
            UserSetting userSetting = userSettingMapper.selectById(atMsg.getToId());
            if(userSetting != null && userSetting.getPushInfo() == 1){
                log.info("正在向用户推送@消息");
                SystemInfo systemInfo = new SystemInfo("有新的用户@您", atMsg.getFromUsername() + "刚刚在评论中@了您");
                websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"atInfo",atMsg.getNumber().toString()).toString());
                log.info("@推送成功");
            }
        }
    }

    public void sendCommentMessage(CommentMsg commentMsg){
        log.info("正在向别的在线用户推送评论");
        WebsocketService websocketService = websocketServiceConcurrentHashMap.get(commentMsg.getToId().toString());
        if(websocketService != null){
            //免打扰处理
            UserSetting userSetting = userSettingMapper.selectById(commentMsg.getToId());
            if(userSetting != null && userSetting.getPushComment() == 1){
                log.info("正在向用户推送评论");
                SystemInfo systemInfo = new SystemInfo("有新的用户评论了您",commentMsg.getUsername() + "：" + commentMsg.getDescription());
                websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"commentInfo",commentMsg.getNumber().toString()).toString());
                log.info("评论推送成功");
            }
        }
    }


    public void sendLikeMessage(LikeMsg likeMsg){
        log.info("正在向别的在线用户推送点赞");
        WebsocketService websocketService = websocketServiceConcurrentHashMap.get(likeMsg.getToId().toString());
        if(websocketService != null){
            //免打扰处理
            UserSetting userSetting = userSettingMapper.selectById(likeMsg.getToId());
            if(userSetting != null && userSetting.getPushLike() == 1){
                log.info("正在向用户推送点赞信息");
                //点赞的区分待完成
                SystemInfo systemInfo;
                if(likeMsg.getType() == 1){
                    systemInfo = new SystemInfo("您有新的cos点赞",likeMsg.getUsername() + "点赞了您的cos");
                }else if(likeMsg.getType() == 2){
                    systemInfo = new SystemInfo("您有新的回答点赞",likeMsg.getUsername() + "点赞了您的回答");
                }else if(likeMsg.getType() == 3){
                    systemInfo = new SystemInfo("您有新的回答评论点赞",likeMsg.getUsername() + "点赞了您的评论");
                }else{
                    systemInfo = new SystemInfo("您有新的cos评论点赞",likeMsg.getUsername() + "点赞了您的cos评论");
                }
                websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"likeInfo",likeMsg.getNumber().toString()).toString());
                log.info("点赞推送成功");
            }
        }
    }


    public void sendAskMsg(AskMsg askMsg){
        log.info("正在向别的用户推送提问");
        WebsocketService websocketService = websocketServiceConcurrentHashMap.get(askMsg.getToId().toString());
        if(websocketService != null){
            log.info("正在向用户推送提问");
            SystemInfo systemInfo = new SystemInfo("您有新的用户提问待回答","用户" + askMsg.getUsername() + "向您提问：" + askMsg.getQuestion());
            websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"askInfo",askMsg.getNumber().toString()).toString());
        }
    }

    public void sendHotCosToEveryOneOnline(HotCosMsg hotCosMsg){
        log.info("正在向所有在线用户推送热门cos");
        for(String x:websocketServiceConcurrentHashMap.keySet()){
            WebsocketService websocketService = websocketServiceConcurrentHashMap.get(x);
            if (websocketService != null){
                log.info("正在向用户" + websocketService.session.getId() + "推送热门cos");
                SystemInfo systemInfo = new SystemInfo("新的热帖推送","用户" + hotCosMsg.getFromUsername() + "发布了新热帖：" + hotCosMsg.getDescription());
                websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"hotCosInfo", hotCosMsg.getNumber().toString()).toString());
                log.info("推送热门cos成功");
            }
        }
    }

    public void sendHotQAToEveryOneOnline(HotQAMsg hotQAMsg){
        log.info("正在向所有在线用户推送热门问答");
        for(String x:websocketServiceConcurrentHashMap.keySet()){
            WebsocketService websocketService = websocketServiceConcurrentHashMap.get(x);
            if (websocketService != null){
                log.info("正在向用户" + websocketService.session.getId() + "推送热门问答");
                SystemInfo systemInfo = new SystemInfo("新的问答推送","用户" + hotQAMsg.getFromUsername() + "发布了新问题：" + hotQAMsg.getTitle());
                websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"hotQAInfo", hotQAMsg.getNumber().toString()).toString());
                log.info("推送热门cos成功");
            }
        }
    }

}
