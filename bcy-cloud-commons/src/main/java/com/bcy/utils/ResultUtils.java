package com.bcy.utils;

import com.alibaba.fastjson.JSONObject;
import com.bcy.pojo.Result;

import java.util.HashMap;

//统一返回结果工具类
public class ResultUtils {

    private static final HashMap<String,Integer> resultMap = new HashMap<>();

    static{
        //成功码 200
        resultMap.put("success",200);
        resultMap.put("logoutSuccess",200);
        resultMap.put("loginSuccess",200);
        resultMap.put("paySuccess",200);
        //一般的失败码 -1
        resultMap.put("userWrong",-1);
        resultMap.put("statusWrong",-1);
        resultMap.put("repeatWrong",-1);
        resultMap.put("existWrong",-1);
        resultMap.put("oldPasswordWrong",-1);
        resultMap.put("addressWrong",-1);
        resultMap.put("typeWrong",-1);
        resultMap.put("fileWrong",-1);
        //登录权限等相关失败码403
        resultMap.put("tokenWrong",403);
        resultMap.put("authorityWrong",403);
        resultMap.put("yzmWrong",403);
        resultMap.put("codeWrong",403);
        resultMap.put("frozenWrong",403);
        //支付过程中失败 -2
        resultMap.put("payFail",-2);
        resultMap.put("refundFail",-2);
    }


    //object是json数据，msg是状态
    public static Result<JSONObject> getResult(JSONObject object, String msg){
        Result<JSONObject> result = new Result<>();
        result.setCode(resultMap.get(msg));
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

}