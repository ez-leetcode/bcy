package com.bcy.websocket.service;

import com.alibaba.fastjson.JSON;
import com.bcy.mq.FansMsg;
import com.bcy.mq.HotCosMsg;
import com.bcy.mq.HotQAMsg;
import com.bcy.pojo.SystemInfo;
import com.bcy.utils.WebsocketResultUtils;
import com.bcy.websocket.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

//总线上的连接
@Component
@Slf4j
@ServerEndpoint(value = "/websocket/{id}")
public class WebsocketService {

    @Autowired
    private RedisUtils redisUtils;

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
        log.info("rabbitmq发送信息成功");
    }

    public void sendFansMessage(FansMsg fansMsg){
        log.info("正在向别的在线用户推送粉丝添加");
        WebsocketService websocketService = websocketServiceConcurrentHashMap.get(fansMsg.getToId().toString());
        if(websocketService != null){
            log.info("正在向用户" + websocketService.session.getId() + "推送粉丝添加信息");
            SystemInfo systemInfo = new SystemInfo("您有新的粉丝关注","用户" + fansMsg.getFromUsername() + "关注了您");
            websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"fansInfo", fansMsg.getFromId()).toString());
            log.info("粉丝添加推送成功");
        }
    }

    public void sendHotCosToEveryOneOnline(HotCosMsg hotCosMsg){
        log.info("正在向所有在线用户推送热门cos");
        for(String x:websocketServiceConcurrentHashMap.keySet()){
            WebsocketService websocketService = websocketServiceConcurrentHashMap.get(x);
            if (websocketService != null){
                log.info("正在向用户" + websocketService.session.getId() + "推送热门cos");
                SystemInfo systemInfo = new SystemInfo("新的热帖推送","用户" + hotCosMsg.getFromUsername() + "发布了新热帖：" + hotCosMsg.getDescription());
                websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"hotCosInfo", hotCosMsg.getNumber()).toString());
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
                websocketService.session.getAsyncRemote().sendText(WebsocketResultUtils.getResult(JSON.parseObject(systemInfo.toString()),"hotQAInfo", hotQAMsg.getNumber()).toString());
                log.info("推送热门cos成功");
            }
        }
    }


}
