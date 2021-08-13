package com.bcy.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class WebsocketResult<T> implements Serializable {

    //类型
    private Integer status;

    //跳转编号
    private String number;

    //消息
    private String msg;

    //数据
    private T data;

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",status);
        jsonObject.put("number",number);
        jsonObject.put("msg",msg);
        jsonObject.put("data",data);
        return jsonObject.toString();
    }

}
