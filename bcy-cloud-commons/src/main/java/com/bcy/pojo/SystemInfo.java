package com.bcy.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SystemInfo implements Serializable {

    private String title;

    private String content;

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title",title);
        jsonObject.put("content",content);
        return jsonObject.toString();
    }

}
