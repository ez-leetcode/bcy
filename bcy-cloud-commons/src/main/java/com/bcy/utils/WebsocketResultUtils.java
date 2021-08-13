package com.bcy.utils;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.WebsocketResult;

import java.util.HashMap;

public class WebsocketResultUtils {

    private static final HashMap<String,Integer> talkResultMap = new HashMap<>();

    static {
        talkResultMap.put("talkInfo",1);
        talkResultMap.put("talkReceive",2);
        talkResultMap.put("systemInfo",3);
        talkResultMap.put("fansInfo",4);
        talkResultMap.put("askInfo",5);
        talkResultMap.put("likeInfo",6);
        talkResultMap.put("commentInfo",7);
        talkResultMap.put("atInfo",8);
    }

    //object是json数据，msg是状态
    public static WebsocketResult<JSONObject> getResult(JSONObject object, String msg,String number){
        WebsocketResult<JSONObject> result = new WebsocketResult<>();
        result.setStatus(talkResultMap.get(msg));
        result.setMsg(msg);
        result.setData(object);
        result.setNumber(number);
        return result;
    }

}