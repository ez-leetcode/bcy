package com.bcy.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T> {

    //返回状态码
    private Integer code;

    //返回的状态消息
    private String msg;

    //返回的数据
    private T data;

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        jsonObject.put("data",data);
        return jsonObject.toString();
    }

}
