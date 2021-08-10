package com.bcy.utils;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.WebsocketResult;

import java.util.HashMap;

public class WebsocketResultUtils {

    private static final HashMap<String,Integer> talkResultMap = new HashMap<>();

    static {
        talkResultMap.put("sendInfoSuccess",1);
        talkResultMap.put("receiveInfoSuccess",2);
        talkResultMap.put("systemInfoSuccess",3);
        talkResultMap.put("fansInfo",4);
    }

    //object是json数据，msg是状态
    public static WebsocketResult<JSONObject> getResult(JSONObject object, String msg,Long number){
        WebsocketResult<JSONObject> result = new WebsocketResult<>();
        result.setStatus(talkResultMap.get(msg));
        result.setMsg(msg);
        result.setData(object);
        result.setNumber(number);
        return result;
    }

}